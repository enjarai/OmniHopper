package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class OxidizableFluidOmniHopperBlock extends FluidOmniHopperBlock implements Oxidizable {

    public OxidizableFluidOmniHopperBlock(OxidationLevel degradationLevel, Settings settings) {
        super(degradationLevel, settings);
    }

    @Override
    public OxidationLevel getDegradationLevel() {
        return degradationLevel;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        tickDegradation(state, world, pos, random);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent();
    }
}
