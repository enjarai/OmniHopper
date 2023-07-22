package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.FluidOmniHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.FluidHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

public class FluidOmniHopperBlock extends OmniHopperBlock {
    protected final Oxidizable.OxidationLevel degradationLevel;

    public FluidOmniHopperBlock(Oxidizable.OxidationLevel degradationLevel, Settings settings) {
        super(settings);
        this.degradationLevel = degradationLevel;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forOxidizableHopperType(FluidHopperBehaviour.TYPE_ID, degradationLevel);
    }

    @Override
    public Identifier modifyTooltipId(Identifier id) {
        return OmniHopper.id("fluid_omnihopper");
    }
}
