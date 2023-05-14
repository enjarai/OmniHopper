package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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
		return state.get(BasicHopperBlock.POINTY_BIT);
	}
}
