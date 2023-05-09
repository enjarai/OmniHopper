package nl.enjarai.omnihopper.blocks;

import net.minecraft.data.client.TextureMap;
import nl.enjarai.omnihopper.blocks.behaviour.FluidHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.entity.FluidOmniHopperBlockEntity;

public class FluidOmniHopperBlock extends OmniHopperBlock {
    public FluidOmniHopperBlock(Settings settings) {
        super(settings);
    }
    // TODO add oxidization and use datagen for models

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forHopperType(FluidHopperBehaviour.TYPE_ID);
    }
}
