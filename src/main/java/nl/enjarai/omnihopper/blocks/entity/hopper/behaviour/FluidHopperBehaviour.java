package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
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
			blockEntity.setChanged();
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
	public void writeData(ValueOutput view) {
		view.store("fluidVariant", FluidVariant.CODEC, fluidStorage.variant);
		view.putLong("amount", fluidStorage.amount);
	}

	@Override
	public void readData(ValueInput view) {
		fluidStorage.variant = view.read("fluidVariant", FluidVariant.CODEC).orElseThrow();
		fluidStorage.amount = view.getLong("amount").orElseThrow();
	}

	@Override
	public long getAmountPerActivation(BlockState targetState) {
		return FluidConstants.BUCKET;
	}

	@Override
	public boolean pickupInWorldObjects(Level world, BlockPos pos, Direction suckyDirection) {
		var fluidPos = pos.relative(suckyDirection);
		var fluid = world.getFluidState(fluidPos);
		var state = world.getBlockState(fluidPos);

		if (fluid.isSource() && state.getBlock() instanceof BucketPickup drainable) {
			try (var transaction = Transaction.openOuter()) {
				long inserted = getStorage().insert(
						FluidVariant.of(fluid.getType()),
						FluidConstants.BUCKET,
						transaction
				);

				if (inserted == FluidConstants.BUCKET && !drainable.pickupBlock(null, world, fluidPos, state).isEmpty()) {
					transaction.commit();

					return true;
				}
			}
		}

		return false;
	}

	@Override
	public InteractionResult onUseWithItem(Player player, InteractionHand hand, BlockHitResult hit) {
		var stack = player.getItemInHand(hand);

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
						player.setItemInHand(hand, ItemUtils.createFilledResult(
								stack, player, containedFluid.getBucket().getDefaultInstance()));
						transaction.commit();

						// We're done now, so play the sound and return success
						player.level().playSound(
								null, player.blockPosition(),
								FluidVariantAttributes.getFillSound(resource),
								SoundSource.BLOCKS, 1.0f, 1.0f
						);
						player.level().gameEvent(null, GameEvent.FLUID_PICKUP, player.blockPosition());
						return InteractionResult.SUCCESS;
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
						player.setItemInHand(hand, ItemUtils.createFilledResult(
								stack, player, BucketItem.getEmptySuccessItem(stack, player)));
						transaction.commit();

						// We're done, so play the sound and return success
						player.level().playSound(
								null, player.blockPosition(),
								FluidVariantAttributes.getEmptySound(resource),
								SoundSource.BLOCKS, 1.0f, 1.0f
						);
						player.level().gameEvent(null, GameEvent.FLUID_PLACE, player.blockPosition());
						return InteractionResult.SUCCESS;
					}
				}
			}
		}

		return super.onUseWithItem(player, hand, hit);
	}
}
