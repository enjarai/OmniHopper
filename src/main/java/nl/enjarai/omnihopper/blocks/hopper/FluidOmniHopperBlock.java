package nl.enjarai.omnihopper.blocks.hopper;

import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.OmniHopper;
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
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.FluidHopperBehaviour;
import nl.enjarai.omnihopper.blocks.entity.hopper.FluidOmniHopperBlockEntity;
import nl.enjarai.omnihopper.util.TextureMapProvider;

public class FluidOmniHopperBlock extends OmniHopperBlock {
    protected final Oxidizable.OxidationLevel degradationLevel;

    public FluidOmniHopperBlock(Oxidizable.OxidationLevel degradationLevel, Settings settings) {
        super(settings);
        this.degradationLevel = degradationLevel;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FluidOmniHopperBlockEntity(pos, state);
    }

    @Override
    public TextureMap getTextureMap() {
        return TextureMapProvider.forOxidizableHopperType(FluidHopperBehaviour.TYPE_ID, degradationLevel);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    public Identifier modifyTooltipId(Identifier id) {
        return OmniHopper.id("fluid_omnihopper");
    }
}
