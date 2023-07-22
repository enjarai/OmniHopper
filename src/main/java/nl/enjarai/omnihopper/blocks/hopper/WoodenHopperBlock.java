package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.entity.hopper.WoodenHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.WoodenHopperBehaviour;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

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
