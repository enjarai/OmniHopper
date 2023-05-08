package nl.enjarai.omnihopper.blocks.entity;

import static nl.enjarai.omnihopper.blocks.OmniHopperBlock.POINTY_BIT;
import static nl.enjarai.omnihopper.blocks.OmniHopperBlock.SUCKY_BIT;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

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
