package nl.enjarai.omnihopper.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.OpenBoxBlock;

@SuppressWarnings("UnstableApiUsage")
public class OpenBoxBlockEntity extends BlockEntity {
    @SuppressWarnings("Convert2Lambda")
    private final Storage<ItemVariant> itemStorage = new InsertionOnlyStorage<>() {
        @Override
        public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            var amount = Math.min(maxAmount, resource.getItem().getMaxCount());
            var stack = resource.toStack((int) amount);
            var world = getWorld();
            var state = getCachedState();
            if (world == null) return 0;

            var direction = state.get(OpenBoxBlock.FACING);
            var vOffset = new Vec3d(0, -0.125, 0);
            var dOffset = new Vec3d(direction.getUnitVector().mul(0.25f));
            var pos = getPos().toCenterPos().add(dOffset).add(vOffset);

            var entity = new ItemEntity(world, pos.x, pos.y, pos.z, stack);
            entity.setVelocity(dOffset);
            world.spawnEntity(entity);

            return amount;
        }
    };

    public OpenBoxBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OPEN_BOX_BLOCK_ENTITY, pos, state);
    }

    public Storage<ItemVariant> getItemStorage() {
        return itemStorage;
    }
}
