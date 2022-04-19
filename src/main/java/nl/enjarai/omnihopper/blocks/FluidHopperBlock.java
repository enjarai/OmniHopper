package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidHopperBlock extends FluidOmniHopperBlock {
    public FluidHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidHopperBlockEntity(pos, state);
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
