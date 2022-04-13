package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
public class OmniHopperBlockEntity extends LootableContainerBlockEntity implements Hopper {
    private DefaultedList<ItemStack> inventory;
    private int transferCooldown;
    private long lastTickTime;

    public OmniHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OMNIHOPPER_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.transferCooldown = -1;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }

        this.transferCooldown = nbt.getInt("TransferCooldown");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }

        nbt.putInt("TransferCooldown", this.transferCooldown);
    }

    public int size() {
        return this.inventory.size();
    }

    public ItemStack removeStack(int slot, int amount) {
        this.checkLootInteraction(null);
        return Inventories.splitStack(this.getInvStackList(), slot, amount);
    }

    public void setStack(int slot, ItemStack stack) {
        this.checkLootInteraction(null);
        this.getInvStackList().set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
    }

    protected Text getContainerName() {
        return new TranslatableText("container.omnihopper");
    }

    public static void serverTick(World world, BlockPos pos, BlockState state, OmniHopperBlockEntity blockEntity) {
        --blockEntity.transferCooldown;
        blockEntity.lastTickTime = world.getTime();
        if (!blockEntity.needsCooldown()) {
            blockEntity.setTransferCooldown(0);
            insertAndExtract(world, pos, state, blockEntity, () -> extract(world, blockEntity, state));
        }
    }

    private static boolean insertAndExtract(World world, BlockPos pos, BlockState state, OmniHopperBlockEntity blockEntity, BooleanSupplier booleanSupplier) {
        if (!world.isClient) {
            if (!blockEntity.needsCooldown() && state.get(HopperBlock.ENABLED)) {
                boolean bl = false;
                if (!blockEntity.isEmpty()) {
                    bl = insert(world, pos, state, blockEntity);
                }

                if (!blockEntity.isFull()) {
                    bl |= booleanSupplier.getAsBoolean();
                }

                if (bl) {
                    blockEntity.setTransferCooldown(8);
                    markDirty(world, pos, state);
                    return true;
                }
            }

        }
        return false;
    }

    private boolean isFull() {
        Iterator<ItemStack> var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(!itemStack.isEmpty() && itemStack.getCount() == itemStack.getMaxCount());

        return false;
    }

    private static boolean insert(World world, BlockPos pos, BlockState state, Inventory inventory) {
        Direction direction = state.get(OmniHopperBlock.POINTY_BIT);
        BlockPos targetPos = pos.offset(direction);
        Storage<ItemVariant> target = ItemStorage.SIDED.find(world, targetPos, direction.getOpposite());

        if (target != null) {
            BlockEntity blockEntityTarget = world.getBlockEntity(pos.offset(direction));
            boolean moved = StorageUtil.move(
                    InventoryStorage.of(inventory, direction),
                    target,
                    iv -> true,
                    1,
                    null
            ) == 1;
            if (moved && blockEntityTarget instanceof OmniHopperBlockEntity otherHopperBlock && !otherHopperBlock.isDisabled()) {
                int j = 0;
                if (inventory instanceof OmniHopperBlockEntity thisHopperBlock) {
                    if (otherHopperBlock.lastTickTime >= thisHopperBlock.lastTickTime) {
                        j = 1;
                    }
                }
                otherHopperBlock.setTransferCooldown(8 - j);
            }
            return moved;
        }
        return false;
    }

    public static boolean extract(World world, Hopper hopper, BlockState state) {
        BlockPos pos = new BlockPos(hopper.getHopperX(), hopper.getHopperY(), hopper.getHopperZ());
        Direction suckyDirection = state.get(OmniHopperBlock.SUCKY_BIT);
        Storage<ItemVariant> source = ItemStorage.SIDED.find(world, pos.add(suckyDirection.getVector()), suckyDirection.getOpposite());

        if (source != null) {
            long moved = StorageUtil.move(
                    source,
                    InventoryStorage.of(hopper, suckyDirection),
                    iv -> true,
                    1,
                    null
            );
            return moved == 1;
        } else {
            Iterator<ItemEntity> entities = getInputItemEntities(world, hopper, suckyDirection).iterator();

            ItemEntity itemEntity;
            do {
                if (!entities.hasNext()) {
                    return false;
                }

                itemEntity = entities.next();
            } while(!extract(hopper, itemEntity, suckyDirection));

            return true;
        }
    }

    public static boolean extract(Inventory inventory, ItemEntity itemEntity, Direction side) {
        boolean bl = false;
        ItemStack itemStack = itemEntity.getStack();

        try (Transaction transaction = Transaction.openOuter()) {
            long amountInserted = InventoryStorage.of(inventory, side).insert(ItemVariant.of(itemStack), itemStack.getCount(), transaction);
            itemStack.decrement((int) amountInserted);
            transaction.commit();
        }

        if (itemStack.isEmpty()) {
            bl = true;
            itemEntity.discard();
        }

        return bl;
    }

    public static List<ItemEntity> getInputItemEntities(World world, Hopper hopper, Direction suckyDirection) {
        return getInputAreaShape(suckyDirection).getBoundingBoxes().stream().flatMap((box) ->
                world.getEntitiesByClass(ItemEntity.class, box.offset(hopper.getHopperX() - 0.5D, hopper.getHopperY() - 0.5D, hopper.getHopperZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream()
        ).collect(Collectors.toList());
    }

    public static VoxelShape getInputAreaShape(Direction suckyDirection) {
        return OmniHopperBlock.SUCKY_AREA[suckyDirection.ordinal()];
    }

    public double getHopperX() {
        return (double)this.pos.getX() + 0.5D;
    }

    public double getHopperY() {
        return (double)this.pos.getY() + 0.5D;
    }

    public double getHopperZ() {
        return (double)this.pos.getZ() + 0.5D;
    }

    private void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    private boolean needsCooldown() {
        return this.transferCooldown > 0;
    }

    private boolean isDisabled() {
        return this.transferCooldown > 8;
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new HopperScreenHandler(syncId, playerInventory, this);
    }
}

