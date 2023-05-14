package nl.enjarai.omnihopper.blocks.entity.hopper;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.gnomecraft.cooldowncoordinator.CooldownCoordinator;
import net.gnomecraft.cooldowncoordinator.CoordinatedCooldown;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.HopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public abstract class HopperBlockEntity<T> extends BlockEntity implements CoordinatedCooldown, NamedScreenHandlerFactory, Nameable {
    protected int transferCooldown;
    protected long lastTickTime;
    private Text customName;
    protected HopperBehaviour<T> behaviour;

    public HopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.transferCooldown = -1;
    }

    public abstract Direction getSuckyDirection(BlockState state);

    public abstract Direction getPointyDirection(BlockState state);

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (nbt.contains("CustomName", 8)) {
            customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
        transferCooldown = nbt.getInt("TransferCooldown");

        behaviour.readNbt(nbt);
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if (customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(customName));
        }
        nbt.putInt("TransferCooldown", transferCooldown);

        behaviour.writeNbt(nbt);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
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
                    setTransferCooldown(behaviour.getCooldown());
                    markDirty(world, pos, state);
                }
            }
        }
    }

    protected boolean insert(World world, BlockPos pos, BlockState state) {
        Direction direction = getPointyDirection(state);
        BlockPos targetPos = pos.offset(direction);
        Storage<T> target = behaviour.getBlockApiLookup().find(world, targetPos, direction.getOpposite());

        if (target != null) {
            BlockEntity blockEntityTarget = world.getBlockEntity(targetPos);
            boolean targetEmpty = StorageUtil.findStoredResource(target) == null;
            if (StorageUtil.move(
                    behaviour.getStorage(),
                    target,
                    iv -> true,
                    behaviour.getAmountPerActivation(world.getBlockState(targetPos)),
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
        BlockPos sourcePos = pos.offset(suckyDirection);
        Storage<T> source = behaviour.getBlockApiLookup().find(world, sourcePos, suckyDirection.getOpposite());

        if (source != null) {
            long moved = StorageUtil.move(
                    source,
                    behaviour.getStorage(),
                    iv -> true,
                    behaviour.getAmountPerActivation(world.getBlockState(sourcePos)),
                    null
            );
            return moved >= 1;
        } else {
            return behaviour.pickupInWorldObjects(world, pos, suckyDirection);
        }
    }

    @Override
    public void notifyCooldown() {
        if (world == null || this.isDisabled()) {
            return;
        }

        if (this.lastTickTime >= world.getTime()) {
            this.transferCooldown = behaviour.getCooldown() - 1;
        } else {
            this.transferCooldown = behaviour.getCooldown();
        }

        this.markDirty();
    }

    public HopperBehaviour<T> getBehaviour() {
        return behaviour;
    }

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
        return this.transferCooldown > behaviour.getCooldown();
    }

    public void setCustomName(Text name) {
        customName = name;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return behaviour.createMenu(syncId, playerInventory, player);
    }
}
