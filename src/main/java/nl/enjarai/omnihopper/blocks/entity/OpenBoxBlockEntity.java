package nl.enjarai.omnihopper.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.OpenBoxBlock;
import nl.enjarai.omnihopper.util.ExecutingInsertionStorage;

@SuppressWarnings("UnstableApiUsage")
public class OpenBoxBlockEntity extends BlockEntity {
    private final ExecutingInsertionStorage<ItemVariant> itemStorage = new ExecutingInsertionStorage<>() {
        @Override
        protected void handleEntry(ItemVariant resource, long amount) {
            var world = getWorld();
            var state = getCachedState();
            // Not sure when world would be null, but feels like something would be wrong if it was.
            if (world == null) throw new IllegalStateException("Can't drop items in a world that doesn't exist!");

            var direction = state.get(OpenBoxBlock.FACING);
            var vOffset = new Vec3d(0, -0.125, 0);
            var dOffset = new Vec3d(direction.getUnitVector().mul(0.25f));
            var pos = getPos().toCenterPos().add(dOffset).add(vOffset);

            while (amount > 0) {
                var stackSize = Math.min(amount, resource.getItem().getMaxCount());
                var stack = resource.toStack((int) stackSize);

                var entity = new ItemEntity(world, pos.x, pos.y, pos.z, stack);
                entity.setVelocity(dOffset);
                world.spawnEntity(entity);

                amount -= stackSize;
            }
        }
    };
    private final ExecutingInsertionStorage<FluidVariant> fluidStorage = new ExecutingInsertionStorage<>() {
        private BlockPos getPlacePos() {
            var world = getWorld();
            var state = getCachedState();
            if (world == null) throw new IllegalStateException("Can't place fluids in a world that doesn't exist!");

            var direction = state.get(OpenBoxBlock.FACING);
            return getPos().offset(direction);
        }

        @Override
        protected long canInsert(FluidVariant resource, long maxAmount) {
            var pos = getPlacePos();
            //noinspection DataFlowIssue
            var blockState = getWorld().getBlockState(pos);
            return maxAmount >= FluidConstants.BLOCK && blockState.canBucketPlace(resource.getFluid()) ? 1000 : 0;
        }

        @Override
        protected void handleEntry(FluidVariant resource, long amount) {

        }
    };

    public OpenBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OPEN_BOX_BLOCK_ENTITY, pos, state);
    }

    public Storage<ItemVariant> getItemStorage() {
        return itemStorage;
    }
}
