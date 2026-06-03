package nl.enjarai.omnihopper.blocks.hopper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BasicHopperBlock extends HopperBlock {
    public static final EnumProperty<Direction> POINTY_BIT;

    static {
        POINTY_BIT = EnumProperty.create("pointy_bit", Direction.class, Direction.values());
    }

    public BasicHopperBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(POINTY_BIT, Direction.DOWN));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state == null
               ? null
               : state
                       .setValue(POINTY_BIT, ctx.getClickedFace().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(POINTY_BIT).ordinal()][Direction.UP.ordinal()];
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return SHAPES_RAYCAST[state.getValue(POINTY_BIT).ordinal()][Direction.UP.ordinal()];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state
                .setValue(POINTY_BIT, rotation.rotate(state.getValue(POINTY_BIT)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(POINTY_BIT)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POINTY_BIT));
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void buildHopperBlockStateModel(BlockModelGenerators blockStateModelGenerator) {
        var variants = PropertyDispatch.C1.initial(BasicHopperBlock.POINTY_BIT);

        variants.generate(
                direction -> BlockModelGenerators.variant(
                        new Variant(
                                ModelLocationUtils.getModelLocation(this, "_" + direction.getName())
                        )
                )
        );

        blockStateModelGenerator.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(this).with(variants)
        );
    }
}
