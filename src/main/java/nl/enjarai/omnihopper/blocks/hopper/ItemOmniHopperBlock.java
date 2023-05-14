package nl.enjarai.omnihopper.blocks.hopper;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.entity.hopper.ItemOmniHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;

public class ItemOmniHopperBlock extends OmniHopperBlock {
    public ItemOmniHopperBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ItemOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forVanillaHopper();
    }
}
