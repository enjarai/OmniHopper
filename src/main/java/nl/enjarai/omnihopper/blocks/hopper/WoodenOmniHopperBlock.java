package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.entity.hopper.WoodenOmniHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.WoodenHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

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
    protected void updateEnabled(World world, BlockPos pos, BlockState state) {
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forHopperType(WoodenHopperBehaviour.TYPE_ID);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.AXE_MINEABLE);
    }
}
