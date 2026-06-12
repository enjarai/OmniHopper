package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.FluidHopperBehaviour;

public class FluidOmniHopperBlockEntity extends OmniHopperBlockEntity<FluidVariant> {
    public FluidOmniHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.FLUID_OMNIHOPPER_BLOCK_ENTITY, blockPos, blockState);
        this.behaviour = new FluidHopperBehaviour(this);
    }

    @Override
    public Component getName() {
        return Component.translatable("container.fluid_omnihopper");
    }
}
