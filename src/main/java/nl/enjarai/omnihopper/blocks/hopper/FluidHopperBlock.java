package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.FluidHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.FluidHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

public class FluidHopperBlock extends BasicHopperBlock {
	protected final WeatheringCopper.WeatherState degradationLevel;

	public FluidHopperBlock(WeatheringCopper.WeatherState degradationLevel, Properties settings) {
		super(settings);
		this.degradationLevel = degradationLevel;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FluidHopperBlockEntity(pos, state);
	}

	@Override
	public TextureMapping getTextureMap() {
		return TextureMapProvider.forOxidizableHopperType(FluidHopperBehaviour.TYPE_ID, degradationLevel);
	}

	@Override
	public Identifier modifyTooltipId(Identifier id) {
		return OmniHopper.id("fluid_hopper");
	}
}
