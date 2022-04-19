package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Direction;

public class FluidHopperBlock extends FluidOmniHopperBlock {
    public FluidHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide().getOpposite();
        return this.getDefaultState()
                .with(SUCKY_BIT, Direction.UP)
                .with(POINTY_BIT, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction)
                .with(ENABLED, true);
    }
}
