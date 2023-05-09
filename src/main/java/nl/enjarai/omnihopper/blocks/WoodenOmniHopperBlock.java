package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.behaviour.WoodenHopperBehaviour;
import nl.enjarai.omnihopper.blocks.entity.WoodenOmniHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

public class WoodenOmniHopperBlock extends OmniHopperBlock {
    public WoodenOmniHopperBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forHopperType(WoodenHopperBehaviour.TYPE_ID);
    }
}
