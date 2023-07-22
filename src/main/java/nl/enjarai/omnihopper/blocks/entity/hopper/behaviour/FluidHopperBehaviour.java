package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;

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
			return FluidConstants.BUCKET;
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
		return FluidConstants.BUCKET;
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

	@Override
	public ActionResult onUse(PlayerEntity player, Hand hand, BlockHitResult hit) {
		var stack = player.getStackInHand(hand);

		// If the player is holding a bucket, we can try to insert or extract fluid
		if (stack.getItem() instanceof BucketItem bucketItem) {
			var bucketFluid = ((BucketItemAccessor) bucketItem).fabric_getFluid();

			// If the bucket is empty and the storage is not, try to extract a buckets worth of fluid
			if (bucketFluid == Fluids.EMPTY && !fluidStorage.isResourceBlank()) {
				// Open a transaction with transfer api
				try (var transaction = Transaction.openOuter()) {
					var resource = fluidStorage.getResource();
					long extracted = getStorage().extract(
							resource,
							FluidConstants.BUCKET,
							transaction
					);

					// We only want to extract exactly one bucket
					if (extracted == FluidConstants.BUCKET) {
						var containedFluid = resource.getFluid();

						// Exchange the stack and commit if successful
						player.setStackInHand(hand, ItemUsage.exchangeStack(
								stack, player, containedFluid.getBucketItem().getDefaultStack()));
						transaction.commit();

						// We're done now, so play the sound and return success
						player.getWorld().playSound(
								null, player.getBlockPos(),
								FluidVariantAttributes.getFillSound(resource),
								SoundCategory.BLOCKS, 1.0f, 1.0f
						);
						player.getWorld().emitGameEvent(null, GameEvent.FLUID_PICKUP, player.getPos());
						return ActionResult.SUCCESS;
					}
				}
			// If the bucket is not empty and the storage has room for one bucket of this fluid, we can try to insert
			} else if (bucketFluid != Fluids.EMPTY && fluidStorage.getCapacity() - fluidStorage.getAmount() >= FluidConstants.BUCKET) {
				// Open a transaction with transfer api
				try (var transaction = Transaction.openOuter()) {
					var resource = FluidVariant.of(bucketFluid);
					long inserted = getStorage().insert(
							resource,
							FluidConstants.BUCKET,
							transaction
					);

					// Only accept the insertion if it was exactly one bucket
					if (inserted == FluidConstants.BUCKET) {

						// Exchange the stack and commit if successful
						player.setStackInHand(hand, ItemUsage.exchangeStack(
								stack, player, BucketItem.getEmptiedStack(stack, player)));
						transaction.commit();

						// We're done, so play the sound and return success
						player.getWorld().playSound(
								null, player.getBlockPos(),
								FluidVariantAttributes.getEmptySound(resource),
								SoundCategory.BLOCKS, 1.0f, 1.0f
						);
						player.getWorld().emitGameEvent(null, GameEvent.FLUID_PLACE, player.getPos());
						return ActionResult.SUCCESS;
					}
				}
			}
		}

		return super.onUse(player, hand, hit);
	}
}
