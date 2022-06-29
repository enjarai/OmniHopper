package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class FluidOmniHopperBlockEntity extends OmniHopperBlockEntity<FluidVariant> {
    public final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return 3 * FluidConstants.BUCKET;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
        }
    };

    public FluidOmniHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.FLUID_OMNIHOPPER_BLOCK_ENTITY, blockPos, blockState);
    }

    public FluidOmniHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public Storage<FluidVariant> getStorage() {
        return fluidStorage;
    }

    @Override
    public BlockApiLookup<Storage<FluidVariant>, Direction> getBlockApiLookup() {
        return FluidStorage.SIDED;
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.put("fluidVariant", fluidStorage.variant.toNbt());
        tag.putLong("amount", fluidStorage.amount);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        fluidStorage.variant = FluidVariant.fromNbt(tag.getCompound("fluidVariant"));
        fluidStorage.amount = tag.getLong("amount");
    }

    @Override
    protected long getAmountPerActivation(BlockState targetState) {
        return FluidConstants.BUCKET / (targetState.isIn(BlockTags.CAULDRONS) ? 1 : 4);
    }

    @Override
    protected boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
        var fluidPos = pos.offset(suckyDirection);
        var fluid = world.getFluidState(fluidPos);
        var state = world.getBlockState(fluidPos);

        if (fluid.isStill() && state.getBlock() instanceof FluidDrainable drainable) {
            try (var transaction = Transaction.openOuter()) {
                long inserted = getStorage().insert(
                        FluidVariant.of(fluid.getFluid()),
                        FluidConstants.BUCKET,
                        transaction
                );

                if (inserted == FluidConstants.BUCKET && !drainable.tryDrainFluid(world, fluidPos, state).isEmpty()) {
                    transaction.commit();

                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Text getName() {
        return Text.translatable("container.fluid_omnihopper");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return null;
    }
}
