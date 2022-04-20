package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.gnomecraft.cooldowncoordinator.CooldownCoordinator;
import net.gnomecraft.cooldowncoordinator.CoordinatedCooldown;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public abstract class HopperBlockEntity<T> extends BlockEntity implements CoordinatedCooldown, NamedScreenHandlerFactory, Nameable {
    protected int transferCooldown;
    protected long lastTickTime;
    private Text customName;

    public HopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.transferCooldown = -1;
    }

    public abstract Storage<T> getStorage();

    public abstract BlockApiLookup<Storage<T>, Direction> getBlockApiLookup();

    public abstract Direction getSuckyDirection(BlockState state);

    public abstract Direction getPointyDirection(BlockState state);

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("CustomName", 8)) {
            customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
        transferCooldown = nbt.getInt("TransferCooldown");
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(customName));
        }
        nbt.putInt("TransferCooldown", transferCooldown);
    }

    protected void tick(World world, BlockPos pos, BlockState state) {
        --transferCooldown;
        lastTickTime = world.getTime();
        if (!needsCooldown()) {
            setTransferCooldown(0);
            insertAndExtract(world, pos, state);
        }
    }

    protected void insertAndExtract(World world, BlockPos pos, BlockState state) {
        if (!world.isClient) {
            if (!needsCooldown() && state.get(HopperBlock.ENABLED)) {
                boolean bl;

                bl = insert(world, pos, state);

                bl |= extract(world, pos, state);

                if (bl) {
                    setTransferCooldown(8);
                    markDirty(world, pos, state);
                }
            }
        }
    }

    protected boolean insert(World world, BlockPos pos, BlockState state) {
        Direction direction = getPointyDirection(state);
        BlockPos targetPos = pos.offset(direction);
        Storage<T> target = getBlockApiLookup().find(world, targetPos, direction.getOpposite());

        if (target != null) {
            BlockEntity blockEntityTarget = world.getBlockEntity(targetPos);
            boolean targetEmpty = StorageUtil.findStoredResource(target, null) == null;
            if (StorageUtil.move(
                    getStorage(),
                    target,
                    iv -> true,
                    getAmountPerActivation(world.getBlockState(targetPos)),
                    null
            ) == 1) {
                if (targetEmpty) {
                    CooldownCoordinator.notify(blockEntityTarget);
                }
                return true;
            }
        }
        return false;
    }

    protected boolean extract(World world, BlockPos pos, BlockState state) {
        Direction suckyDirection = getSuckyDirection(state);
        BlockPos targetPos = pos.offset(suckyDirection);
        Storage<T> source = getBlockApiLookup().find(world, targetPos, suckyDirection.getOpposite());

        if (source != null) {
            long moved = StorageUtil.move(
                    source,
                    getStorage(),
                    iv -> true,
                    getAmountPerActivation(world.getBlockState(targetPos)),
                    null
            );
            return moved >= 1;
        } else {
            return pickupInWorldObjects(world, pos, suckyDirection);
        }
    }

    protected boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
        return false;
    }

    @Override
    public void notifyCooldown() {
        if (world == null || this.isDisabled()) {
            return;
        }

        if (this.lastTickTime >= world.getTime()) {
            this.transferCooldown = 7;
        } else {
            this.transferCooldown = 8;
        }

        this.markDirty();
    }

    protected abstract long getAmountPerActivation(BlockState targetState);

    @Nullable
    @Override
    public Text getCustomName() {
        return customName;
    }

    @Override
    public Text getDisplayName() {
        return getCustomName() != null ? getCustomName() : getName();
    }

    protected void setTransferCooldown(int transferCooldown) {
        this.transferCooldown = transferCooldown;
    }

    protected boolean needsCooldown() {
        return this.transferCooldown > 0;
    }

    protected boolean isDisabled() {
        return this.transferCooldown > 8;
    }

    public void setCustomName(Text name) {
        customName = name;
    }
}
