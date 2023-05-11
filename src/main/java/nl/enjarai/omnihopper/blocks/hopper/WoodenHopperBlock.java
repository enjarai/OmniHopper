package nl.enjarai.omnihopper.blocks.hopper;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.WoodenHopperBehaviour;
import nl.enjarai.omnihopper.blocks.entity.hopper.WoodenHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;

public class WoodenHopperBlock extends BasicHopperBlock {
    public WoodenHopperBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forHopperType(WoodenHopperBehaviour.TYPE_ID);
    }
}
