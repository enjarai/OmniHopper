package nl.enjarai.omnihopper.blocks.behaviour;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.HopperBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class FluidHopperBehaviour extends HopperBehaviour<FluidVariant> {
	public static final Identifier TYPE_ID = OmniHopper.id("fluid_hopper");

	private final SingleVariantStorage<FluidVariant> fluidStorage = new SingleVariantStorage<>() {
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
			blockEntity.markDirty();
		}
	};

	public FluidHopperBehaviour(HopperBlockEntity<?> blockEntity) {
		super(TYPE_ID, FluidStorage.SIDED, blockEntity);
	}

	@Override
	public Storage<FluidVariant> getStorage() {
		return fluidStorage;
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		tag.put("fluidVariant", fluidStorage.variant.toNbt());
		tag.putLong("amount", fluidStorage.amount);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		fluidStorage.variant = FluidVariant.fromNbt(tag.getCompound("fluidVariant"));
		fluidStorage.amount = tag.getLong("amount");
	}

	@Override
	public long getAmountPerActivation(BlockState targetState) {
		return FluidConstants.BUCKET / (targetState.isIn(BlockTags.CAULDRONS) ? 1 : 4);
	}

	@Override
	public boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
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
}
