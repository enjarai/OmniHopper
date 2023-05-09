package nl.enjarai.omnihopper.blocks;

import net.minecraft.data.client.TextureMap;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.entity.ItemOmniHopperBlockEntity;

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
