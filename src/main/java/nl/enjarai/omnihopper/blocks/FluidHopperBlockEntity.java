package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class FluidHopperBlockEntity extends FluidOmniHopperBlockEntity {
    public FluidHopperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @Override
    public Text getName() {
        return new TranslatableText("container.fluid_hopper");
    }
}
