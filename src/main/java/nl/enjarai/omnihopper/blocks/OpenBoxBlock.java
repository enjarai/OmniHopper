package nl.enjarai.omnihopper.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Pair;
import net.minecraft.util.math.AxisRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import nl.enjarai.omnihopper.blocks.entity.OpenBoxBlockEntity;
import nl.enjarai.omnihopper.util.DatagenBlock;
import nl.enjarai.omnihopper.util.HasTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class OpenBoxBlock extends BlockWithEntity implements DatagenBlock, HasTooltip, Waterloggable {
    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
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
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.UP).with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(FACING, WATERLOGGED));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(FACING).ordinal()];
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        var fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        return getDefaultState().with(FACING, ctx.getSide()).with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
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

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return Waterloggable.super.tryFillWithFluid(world, pos, state, fluidState);
    }

    @Override
    public boolean canFillWithFluid(@org.jspecify.annotations.Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return Waterloggable.super.canFillWithFluid(filler, world, pos, state, fluid);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (state.get(WATERLOGGED)) {
            tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        var variants = BlockStateVariantMap.SingleProperty.models(OpenBoxBlock.FACING);

        variants.generate(
                direction -> {
                    var rotations = rotationFromDirection(direction);
                    return BlockStateModelGenerator.createWeightedVariant(
                            new ModelVariant(
                                    ModelIds.getBlockModelId(this),
                                    new ModelVariant.ModelState(
                                            rotations.getLeft(),
                                            rotations.getRight(),
                                            AxisRotation.R0,
                                            false
                                    )
                            )

                    );
                }
        );

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockModelDefinitionCreator.of(this).with(variants)
        );
    }

    private static Pair<AxisRotation, AxisRotation> rotationFromDirection(Direction dir) {
        return switch (dir) {
            case DOWN -> new Pair<>(AxisRotation.R180, AxisRotation.R0);
            case UP -> new Pair<>(AxisRotation.R0, AxisRotation.R0);
            case NORTH -> new Pair<>(AxisRotation.R90, AxisRotation.R0);
            case SOUTH -> new Pair<>(AxisRotation.R270, AxisRotation.R0);
            case WEST -> new Pair<>(AxisRotation.R270, AxisRotation.R90);
            case EAST -> new Pair<>(AxisRotation.R90, AxisRotation.R90);
        };
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return null;
    }
}
