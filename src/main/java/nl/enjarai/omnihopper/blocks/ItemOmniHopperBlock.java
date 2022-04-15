package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class ItemOmniHopperBlock extends OmniHopperBlock {

    public ItemOmniHopperBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POINTY_BIT, Direction.DOWN).with(SUCKY_BIT, Direction.UP));
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemOmniHopperBlockEntity(pos, state);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ItemOmniHopperBlockEntity hopperBlockEntity) {
                ItemScatterer.spawn(world, pos, hopperBlockEntity.inventory);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
