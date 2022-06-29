package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class ItemOmniHopperBlockEntity extends OmniHopperBlockEntity<ItemVariant> {
    public final SimpleInventory inventory = new SimpleInventory(5) {
        @Override
        public void markDirty() {
            ItemOmniHopperBlockEntity.this.markDirty();
        }
    };
    private final InventoryStorage inventoryWrapper = InventoryStorage.of(inventory, null);

    public ItemOmniHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OMNIHOPPER_BLOCK_ENTITY, pos, state);
    }

    @Override
    public Storage<ItemVariant> getStorage() {
        return inventoryWrapper;
    }

    @Override
    protected long getAmountPerActivation(BlockState targetState) {
        return 1;
    }

    @Override
    public BlockApiLookup<Storage<ItemVariant>, Direction> getBlockApiLookup() {
        return ItemStorage.SIDED;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        inventory.stacks = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        Inventories.readNbt(nbt, inventory.stacks);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory.stacks);
    }

    @Override
    protected boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
        Iterator<ItemEntity> entities = getInputItemEntities(world, pos, suckyDirection).iterator();

        ItemEntity itemEntity;
        do {
            if (!entities.hasNext()) {
                return false;
            }

            itemEntity = entities.next();
        } while(!suckItem(itemEntity));

        return true;
    }

    private boolean suckItem(ItemEntity itemEntity) {
        boolean bl = false;
        ItemStack itemStack = itemEntity.getStack();

        try (Transaction transaction = Transaction.openOuter()) {
            long amountInserted = getStorage().insert(ItemVariant.of(itemStack), itemStack.getCount(), transaction);
            itemStack.decrement((int) amountInserted);
            transaction.commit();
        }

        if (itemStack.isEmpty()) {
            bl = true;
            itemEntity.discard();
        }

        return bl;
    }

    private static List<ItemEntity> getInputItemEntities(World world, BlockPos pos, Direction suckyDirection) {
        return getInputAreaShape(suckyDirection).getBoundingBoxes().stream().flatMap((box) ->
                world.getEntitiesByClass(ItemEntity.class, box.offset(pos), EntityPredicates.VALID_ENTITY).stream()
        ).collect(Collectors.toList());
    }

    private static VoxelShape getInputAreaShape(Direction suckyDirection) {
        return ItemOmniHopperBlock.SUCKY_AREA[suckyDirection.ordinal()];
    }

    @Override
    public Text getName() {
        return Text.translatable("container.omnihopper");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new HopperScreenHandler(syncId, inv, inventory);
    }
}

