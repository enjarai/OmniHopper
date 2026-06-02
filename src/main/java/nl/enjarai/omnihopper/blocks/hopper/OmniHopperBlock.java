package nl.enjarai.omnihopper.blocks.hopper;

import com.mojang.math.Quadrant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.model.Variant;
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
import nl.enjarai.omnihopper.datagen.HopperRotation;

public abstract class OmniHopperBlock extends HopperBlock {
    public static final EnumProperty<Direction> POINTY_BIT;
    public static final EnumProperty<Direction> SUCKY_BIT;

    static {
        POINTY_BIT = EnumProperty.create("pointy_bit",Direction.class, Direction.values());
        SUCKY_BIT = EnumProperty.create("sucky_bit", Direction.class, Direction.values());
    }

    public OmniHopperBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(POINTY_BIT, Direction.DOWN).setValue(SUCKY_BIT, Direction.UP));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state == null ? null : state
                .setValue(POINTY_BIT, ctx.getClickedFace().getOpposite())
                .setValue(SUCKY_BIT, ctx.getNearestLookingDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(POINTY_BIT).ordinal()][state.getValue(SUCKY_BIT).ordinal()];
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return SHAPES_RAYCAST[state.getValue(POINTY_BIT).ordinal()][state.getValue(SUCKY_BIT).ordinal()];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state
                .setValue(POINTY_BIT, rotation.rotate(state.getValue(POINTY_BIT)))
                .setValue(SUCKY_BIT, rotation.rotate(state.getValue(SUCKY_BIT)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(POINTY_BIT)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POINTY_BIT).add(SUCKY_BIT));
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected void buildHopperBlockStateModel(BlockModelGenerators blockStateModelGenerator) {
        var variants = PropertyDispatch.C2.initial(OmniHopperBlock.POINTY_BIT, OmniHopperBlock.SUCKY_BIT);

        variants.generate(
                (pointy, sucky) -> {
                    var settings = HopperRotation.getFor(sucky, pointy);
                    return BlockModelGenerators.variant(
                            new Variant(
                                    ModelLocationUtils.getModelLocation(this, "_" + settings.modelDirection().getName()),
                                    new Variant.SimpleModelState(
                                            settings.rotX(),
                                            settings.rotY(),
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
}
