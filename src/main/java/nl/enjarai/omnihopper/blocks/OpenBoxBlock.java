package nl.enjarai.omnihopper.blocks;

import java.util.Set;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import nl.enjarai.omnihopper.blocks.entity.OpenBoxBlockEntity;
import nl.enjarai.omnihopper.util.DatagenBlock;

import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class OpenBoxBlock extends BlockWithEntity implements DatagenBlock {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final VoxelShape[] SHAPES = new VoxelShape[6];

    static {
        for (var direction : Direction.values()) {
            var index = direction.ordinal();

            var squish = direction.getVector().multiply(2);
            var offset = direction.getVector().multiply(-3);
            var shape = createCuboidShape(
                    1 + Math.abs(squish.getX()) + offset.getX(),
                    1 + Math.abs(squish.getY()) + offset.getY(),
                    1 + Math.abs(squish.getZ()) + offset.getZ(),
                    15 - Math.abs(squish.getX()) + offset.getX(),
                    15 - Math.abs(squish.getY()) + offset.getY(),
                    15 - Math.abs(squish.getZ()) + offset.getZ()
            );

            SHAPES[index] = shape;
        }
    }

    public OpenBoxBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(FACING).ordinal()];
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new OpenBoxBlockEntity(pos, state);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.AXE_MINEABLE);
    }
}
