package nl.enjarai.omnihopper.blocks.hopper;

import nl.enjarai.omnihopper.blocks.entity.hopper.WoodenOmniHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.WoodenHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WoodenOmniHopperBlock extends OmniHopperBlock {
    public WoodenOmniHopperBlock(Properties settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenOmniHopperBlockEntity(pos, state);
    }

    @Override
    protected void updateEnabled(Level world, BlockPos pos, BlockState state) {
    }

    @Override
    public TextureMapping getTextureMap() {
        return TextureMapProvider.forHopperType(WoodenHopperBehaviour.TYPE_ID);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.MINEABLE_WITH_AXE);
    }
}
