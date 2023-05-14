package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.FluidHopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public class FluidHopperBlockEntity extends BasicHopperBlockEntity<FluidVariant> {
    public FluidHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.FLUID_HOPPER_BLOCK_ENTITY, blockPos, blockState);
        this.behaviour = new FluidHopperBehaviour(this);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.fluid_hopper");
    }
}
