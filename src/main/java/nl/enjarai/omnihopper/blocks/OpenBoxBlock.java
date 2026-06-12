package nl.enjarai.omnihopper.blocks;

import com.mojang.math.Quadrant;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.enjarai.omnihopper.blocks.entity.OpenBoxBlockEntity;
import nl.enjarai.omnihopper.util.DatagenBlock;
import nl.enjarai.omnihopper.util.HasTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class OpenBoxBlock extends BaseEntityBlock implements DatagenBlock, HasTooltip, SimpleWaterloggedBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final VoxelShape[] SHAPES = new VoxelShape[6];

    static {
        for (var direction : Direction.values()) {
            var index = direction.ordinal();

            var squish = direction.getUnitVec3i().multiply(2);
            var offset = direction.getUnitVec3i().multiply(-3);
            var shape = box(
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

    public OpenBoxBlock(Properties settings) {
        super(settings);
        registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, WATERLOGGED));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).ordinal()];
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        var fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return defaultBlockState().setValue(FACING, ctx.getClickedFace()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new OpenBoxBlockEntity(pos, state);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.MINEABLE_WITH_AXE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor world, BlockPos pos, BlockState state, FluidState fluidState) {
        return SimpleWaterloggedBlock.super.placeLiquid(world, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable LivingEntity filler, BlockGetter world, BlockPos pos, BlockState state, Fluid fluid) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(filler, world, pos, state, fluid);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (state.getValue(WATERLOGGED)) {
            tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        var variants = PropertyDispatch.C1.initial(OpenBoxBlock.FACING);

        variants.generate(
                direction -> {
                    var rotations = rotationFromDirection(direction);
                    return BlockModelGenerators.variant(
                            new Variant(
                                    ModelLocationUtils.getModelLocation(this),
                                    new Variant.SimpleModelState(
                                            rotations.getA(),
                                            rotations.getB(),
                                            Quadrant.R0,
                                            false
                                    )
                            )

                    );
                }
        );

        blockStateModelGenerator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(this).with(variants)
        );
    }

    private static Tuple<Quadrant, Quadrant> rotationFromDirection(Direction dir) {
        return switch (dir) {
            case DOWN -> new Tuple<>(Quadrant.R180, Quadrant.R0);
            case UP -> new Tuple<>(Quadrant.R0, Quadrant.R0);
            case NORTH -> new Tuple<>(Quadrant.R90, Quadrant.R0);
            case SOUTH -> new Tuple<>(Quadrant.R270, Quadrant.R0);
            case WEST -> new Tuple<>(Quadrant.R270, Quadrant.R90);
            case EAST -> new Tuple<>(Quadrant.R90, Quadrant.R90);
        };
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}
