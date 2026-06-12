package nl.enjarai.omnihopper.blocks.entity.hopper;

import static nl.enjarai.omnihopper.blocks.hopper.OmniHopperBlock.POINTY_BIT;
import static nl.enjarai.omnihopper.blocks.hopper.OmniHopperBlock.SUCKY_BIT;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class OmniHopperBlockEntity<T> extends HopperBlockEntity<T> {
    public OmniHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Direction getSuckyDirection(BlockState state) {
        return state.getValue(SUCKY_BIT);
    }

    @Override
    public Direction getPointyDirection(BlockState state) {
        return state.getValue(POINTY_BIT);
    }
}
