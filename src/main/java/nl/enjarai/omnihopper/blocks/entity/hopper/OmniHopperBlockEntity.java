package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static nl.enjarai.omnihopper.blocks.hopper.OmniHopperBlock.POINTY_BIT;
import static nl.enjarai.omnihopper.blocks.hopper.OmniHopperBlock.SUCKY_BIT;

public abstract class OmniHopperBlockEntity<T> extends HopperBlockEntity<T> {
    public OmniHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Direction getSuckyDirection(BlockState state) {
        return state.get(SUCKY_BIT);
    }

    @Override
    public Direction getPointyDirection(BlockState state) {
        return state.get(POINTY_BIT);
    }
}
