package nl.enjarai.omnihopper.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.TextureMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.behaviour.FluidHopperBehaviour;
import nl.enjarai.omnihopper.blocks.entity.FluidOmniHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;

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

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }
}
