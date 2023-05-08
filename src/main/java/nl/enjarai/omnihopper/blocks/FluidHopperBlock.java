package nl.enjarai.omnihopper.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.entity.FluidHopperBlockEntity;

public class FluidHopperBlock extends BasicHopperBlock {
    public FluidHopperBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidHopperBlockEntity(pos, state);
    }
}
