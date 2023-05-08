package nl.enjarai.omnihopper.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.behaviour.FluidHopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public class FluidOmniHopperBlockEntity extends OmniHopperBlockEntity<FluidVariant> {
    public FluidOmniHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.FLUID_OMNIHOPPER_BLOCK_ENTITY, blockPos, blockState);
        this.behaviour = new FluidHopperBehaviour(this);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.fluid_omnihopper");
    }
}
