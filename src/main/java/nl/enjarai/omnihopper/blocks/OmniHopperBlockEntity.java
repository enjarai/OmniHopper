package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
public class OmniHopperBlockEntity extends LootableContainerBlockEntity implements Hopper {
    private DefaultedList<ItemStack> inventory;
    private int transferCooldown;

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
            long moved = StorageUtil.move(
                    InventoryStorage.of(inventory, direction),
                    target,
                    iv -> true,
                    1,
                    null
            );
            return moved == 1;
        }
        return false;
    }

    private static IntStream getAvailableSlots(Inventory inventory, Direction side) {
        return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory)inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
    }

    private static boolean isInventoryFull(Inventory inventory, Direction direction) { // TODO cleanup
        return getAvailableSlots(inventory, direction).allMatch((slot) -> {
            ItemStack itemStack = inventory.getStack(slot);
            return itemStack.getCount() >= itemStack.getMaxCount();
        });
    }

    private static boolean isInventoryEmpty(Inventory inv, Direction facing) {
        return getAvailableSlots(inv, facing).allMatch((slot) -> {
            return inv.getStack(slot).isEmpty();
        });
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

    private static boolean extract(Hopper hopper, Inventory inventory, int slot, Direction side) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!itemStack.isEmpty() && canExtract(inventory, itemStack, slot, side)) {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = transfer(inventory, hopper, inventory.removeStack(slot, 1), (Direction)null);
            if (itemStack3.isEmpty()) {
                inventory.markDirty();
                return true;
            }

            inventory.setStack(slot, itemStack2);
        }

        return false;
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

    public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side) {
        if (to instanceof SidedInventory && side != null) {
            SidedInventory sidedInventory = (SidedInventory)to;
            int[] is = sidedInventory.getAvailableSlots(side);

            for(int i = 0; i < is.length && !stack.isEmpty(); ++i) {
                stack = transfer(from, to, stack, is[i], side);
            }
        } else {
            int j = to.size();

            for(int k = 0; k < j && !stack.isEmpty(); ++k) {
                stack = transfer(from, to, stack, k, side);
            }
        }

        return stack;
    }

    private static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
        if (!inventory.isValid(slot, stack)) {
            return false;
        } else {
            return !(inventory instanceof SidedInventory) || ((SidedInventory)inventory).canInsert(slot, stack, side);
        }
    }

    private static boolean canExtract(Inventory inv, ItemStack stack, int slot, Direction facing) {
        return !(inv instanceof SidedInventory) || ((SidedInventory)inv).canExtract(slot, stack, facing);
    }

    private static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot, @Nullable Direction side) {
        ItemStack itemStack = to.getStack(slot);
        if (canInsert(to, stack, slot, side)) {
            boolean bl = false;
            boolean bl2 = to.isEmpty();
            if (itemStack.isEmpty()) {
                to.setStack(slot, stack);
                stack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack, stack)) {
                int i = stack.getMaxCount() - itemStack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.decrement(j);
                itemStack.increment(j);
                bl = j > 0;
            }

            if (bl) {
                if (bl2 && to instanceof HopperBlockEntity) {
                    HopperBlockEntity hopperBlockEntity = (HopperBlockEntity)to;
//                    if (!hopperBlockEntity.isDisabled()) { // TODO use some of this to make delay better
//                        int j = 0;
//                        if (from instanceof net.minecraft.block.entity.HopperBlockEntity) {
//                            net.minecraft.block.entity.HopperBlockEntity hopperBlockEntity2 = (net.minecraft.block.entity.HopperBlockEntity)from;
//                            if (hopperBlockEntity.lastTickTime >= hopperBlockEntity2.lastTickTime) {
//                                j = 1;
//                            }
//                        }
//
//                        hopperBlockEntity.setTransferCooldown(8 - j);
//                    }
                }

                to.markDirty();
            }
        }

        return stack;
    }

    @Nullable
    private static Inventory getOutputInventory(World world, BlockPos pos, BlockState state) {
        Direction direction = (Direction)state.get(HopperBlock.FACING);
        return getInventoryAt(world, pos.offset(direction));
    }

    @Nullable
    private static Inventory getInputInventory(World world, Hopper hopper) {
        return getInventoryAt(world, hopper.getHopperX(), hopper.getHopperY() + 1.0D, hopper.getHopperZ());
    }

    public static List<ItemEntity> getInputItemEntities(World world, Hopper hopper, Direction suckyDirection) {
        return getInputAreaShape(suckyDirection).getBoundingBoxes().stream().flatMap((box) ->
                world.getEntitiesByClass(ItemEntity.class, box.offset(hopper.getHopperX() - 0.5D, hopper.getHopperY() - 0.5D, hopper.getHopperZ() - 0.5D), EntityPredicates.VALID_ENTITY).stream()
        ).collect(Collectors.toList());
    }

    public static VoxelShape getInputAreaShape(Direction suckyDirection) {
        return OmniHopperBlock.SUCKY_AREA[suckyDirection.ordinal()];
    }

    @Nullable
    public static Inventory getInventoryAt(World world, BlockPos pos) {
        return getInventoryAt(world, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D);
    }

    @Nullable
    private static Inventory getInventoryAt(World world, double x, double y, double z) {
        Inventory inventory = null;
        BlockPos blockPos = new BlockPos(x, y, z);
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();
        if (block instanceof InventoryProvider) {
            inventory = ((InventoryProvider)block).getInventory(blockState, world, blockPos);
        } else if (blockState.hasBlockEntity()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Inventory) {
                inventory = (Inventory)blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getInventory((ChestBlock)block, blockState, world, blockPos, true);
                }
            }
        }

        if (inventory == null) {
            List<Entity> list = world.getOtherEntities((Entity)null, new Box(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntityPredicates.VALID_INVENTORIES);
            if (!list.isEmpty()) {
                inventory = (Inventory)list.get(world.random.nextInt(list.size()));
            }
        }

        return (Inventory)inventory;
    }

    private static boolean canMergeItems(ItemStack first, ItemStack second) {
        if (!first.isOf(second.getItem())) {
            return false;
        } else if (first.getDamage() != second.getDamage()) {
            return false;
        } else if (first.getCount() > first.getMaxCount()) {
            return false;
        } else {
            return ItemStack.areNbtEqual(first, second);
        }
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

