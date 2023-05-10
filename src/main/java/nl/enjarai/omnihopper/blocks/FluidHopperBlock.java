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
import nl.enjarai.omnihopper.blocks.entity.FluidHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;

public class FluidHopperBlock extends BasicHopperBlock {
    public FluidHopperBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidHopperBlockEntity(pos, state);
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
