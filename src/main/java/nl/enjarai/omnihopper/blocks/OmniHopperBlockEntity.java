package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static nl.enjarai.omnihopper.blocks.OmniHopperBlock.POINTY_BIT;
import static nl.enjarai.omnihopper.blocks.OmniHopperBlock.SUCKY_BIT;

public abstract class OmniHopperBlockEntity<T> extends HopperBlockEntity<T> {
    public OmniHopperBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
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
