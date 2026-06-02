package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;

public class OxidizableFluidOmniHopperBlock extends FluidOmniHopperBlock implements WeatheringCopper {

    public OxidizableFluidOmniHopperBlock(WeatherState degradationLevel, Properties settings) {
        super(degradationLevel, settings);
    }

    @Override
    public WeatherState getAge() {
        return degradationLevel;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        changeOverTime(state, world, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }
}
