package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.gnomecraft.cooldowncoordinator.CooldownCoordinator;
import net.gnomecraft.cooldowncoordinator.CoordinatedCooldown;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.HopperBehaviour;
import org.jetbrains.annotations.Nullable;

public abstract class HopperBlockEntity<T> extends BlockEntity implements CoordinatedCooldown, MenuProvider, Nameable {
    protected int transferCooldown;
    protected long lastTickTime;
    private Component customName;
    protected HopperBehaviour<T> behaviour;

    public HopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.transferCooldown = -1;
    }

    public abstract Direction getSuckyDirection(BlockState state);

    public abstract Direction getPointyDirection(BlockState state);

    @Override
    protected void loadAdditional(ValueInput view) {
        super.loadAdditional(view);
        view.read("CustomName", ComponentSerialization.CODEC).ifPresent(name -> customName = name);
        transferCooldown = view.getIntOr("TransferCooldown", 0);
        behaviour.readData(view);
    }

    @Override
    protected void saveAdditional(ValueOutput view) {
        super.saveAdditional(view);
        if (customName != null) {
            view.store("CustomName", ComponentSerialization.CODEC, customName);
        }
        view.putInt("TransferCooldown", transferCooldown);
        behaviour.writeData(view);
    }

    public void tick(Level world, BlockPos pos, BlockState state) {
        --transferCooldown;
        lastTickTime = world.getGameTime();
        if (!needsCooldown()) {
            setTransferCooldown(0);
            insertAndExtract(world, pos, state);
        }
    }

    protected void insertAndExtract(Level world, BlockPos pos, BlockState state) {
        if (!world.isClientSide()) {
            if (!needsCooldown() && state.getValue(HopperBlock.ENABLED)) {
                boolean bl;

                bl = insert(world, pos, state);

                bl |= extract(world, pos, state);

                if (bl) {
                    setTransferCooldown(behaviour.getCooldown());
                    setChanged(world, pos, state);
                }
            }
        }
    }

    protected boolean insert(Level world, BlockPos pos, BlockState state) {
        Direction direction = getPointyDirection(state);
        BlockPos targetPos = pos.relative(direction);
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

    protected boolean extract(Level world, BlockPos pos, BlockState state) {
        Direction suckyDirection = getSuckyDirection(state);
        BlockPos sourcePos = pos.relative(suckyDirection);
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
        if (level == null || this.isDisabled()) {
            return;
        }

        if (this.lastTickTime >= level.getGameTime()) {
            this.transferCooldown = behaviour.getCooldown() - 1;
        } else {
            this.transferCooldown = behaviour.getCooldown();
        }

        this.setChanged();
    }

    public HopperBehaviour<T> getBehaviour() {
        return behaviour;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return customName;
    }

    @Override
    public Component getDisplayName() {
        return getCustomName() != null
               ? getCustomName()
               : getName();
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

    public void setCustomName(Component name) {
        customName = name;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return behaviour.createMenu(syncId, playerInventory, player);
    }

    public InteractionResult onUseWithItem(Player player, InteractionHand hand, BlockHitResult hit) {
        return behaviour.onUseWithItem(player, hand, hit);
    }
}
