package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class FluidHopperBlockEntity extends FluidOmniHopperBlockEntity {
    public FluidHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlocks.FLUID_HOPPER_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.fluid_hopper");
    }
}
