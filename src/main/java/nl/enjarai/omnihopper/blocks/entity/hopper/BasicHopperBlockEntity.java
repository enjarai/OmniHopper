package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.blocks.hopper.BasicHopperBlock;

public abstract class BasicHopperBlockEntity<T> extends OmniHopperBlockEntity<T> {
	public BasicHopperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public Direction getSuckyDirection(BlockState state) {
		return Direction.UP;
	}

	@Override
	public Direction getPointyDirection(BlockState state) {
		return state.getValue(BasicHopperBlock.POINTY_BIT);
	}
}
