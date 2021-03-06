package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

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
}
