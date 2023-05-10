package nl.enjarai.omnihopper.blocks;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
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
	protected final Oxidizable.OxidationLevel degradationLevel;

	public FluidHopperBlock(Oxidizable.OxidationLevel degradationLevel, Settings settings) {
		super(settings);
		this.degradationLevel = degradationLevel;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FluidHopperBlockEntity(pos, state);
	}

	@Override
	public TextureMap getTextureMap() {
		return TextureMapProvider.forOxidizableHopperType(FluidHopperBehaviour.TYPE_ID, degradationLevel);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return ActionResult.PASS;
	}
}
