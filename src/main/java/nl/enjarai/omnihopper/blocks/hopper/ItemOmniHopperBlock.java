package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.blocks.entity.hopper.ItemOmniHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

public class ItemOmniHopperBlock extends OmniHopperBlock {
    public ItemOmniHopperBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMapping getTextureMap() {
        return TextureMapProvider.forVanillaHopper();
    }
}
