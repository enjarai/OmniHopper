package nl.enjarai.omnihopper.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public abstract class OmniHopperBlock extends HopperBlock {
    public static final EnumProperty<Direction> POINTY_BIT;
    public static final EnumProperty<Direction> SUCKY_BIT;

    static {
        POINTY_BIT = DirectionProperty.of("pointy_bit", Direction.values());
        SUCKY_BIT = DirectionProperty.of("sucky_bit", Direction.values());
    }

    public OmniHopperBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(POINTY_BIT, Direction.DOWN).with(SUCKY_BIT, Direction.UP));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : state
                .with(POINTY_BIT, ctx.getSide().getOpposite())
                .with(SUCKY_BIT, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPES[state.get(OmniHopperBlock.POINTY_BIT).ordinal()][state.get(OmniHopperBlock.SUCKY_BIT).ordinal()];
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPES_RAYCAST[state.get(OmniHopperBlock.POINTY_BIT).ordinal()][state.get(OmniHopperBlock.SUCKY_BIT).ordinal()];
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state
                .with(POINTY_BIT, rotation.rotate(state.get(POINTY_BIT)))
                .with(SUCKY_BIT, rotation.rotate(state.get(SUCKY_BIT)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(POINTY_BIT)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(POINTY_BIT).add(SUCKY_BIT));
    }
}
