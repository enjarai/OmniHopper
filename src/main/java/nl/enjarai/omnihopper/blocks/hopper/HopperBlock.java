package nl.enjarai.omnihopper.blocks.hopper;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.client.data.*;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.ItemHopperBehaviour;
import nl.enjarai.omnihopper.util.DatagenBlock;
import nl.enjarai.omnihopper.util.HasTooltip;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

public abstract class HopperBlock extends BaseEntityBlock implements DatagenBlock, TextureMapProvider, HasTooltip {
    public static final BooleanProperty ENABLED;
    public static final VoxelShape[] SUCKY_AREA;
    private static final VoxelShape MIDDLE_SHAPE;
    private static final VoxelShape POINTY_SHAPE;
    protected static final VoxelShape[][] SHAPES;
    protected static final VoxelShape[][] SHAPES_RAYCAST;

    static {
        ENABLED = BlockStateProperties.ENABLED;

        var defaultShapes = new VoxelShape[]{
                Block.box(0, 0, 0, 16, 6, 16),
                Block.box(0, 10, 0, 16, 16, 16),

                Block.box(0, 0, 0, 16, 16, 6),
                Block.box(0, 0, 10, 16, 16, 16),

                Block.box(0, 0, 0, 6, 16, 16),
                Block.box(10, 0, 0, 16, 16, 16),
        };
        var insideShapes = new VoxelShape[]{
                Block.box(2, 0, 2, 14, 5, 14),
                Block.box(2, 11, 2, 14, 16, 14),

                Block.box(2, 2, 0, 14, 14, 5),
                Block.box(2, 2, 11, 14, 14, 16),

                Block.box(0, 2, 2, 5, 14, 14),
                Block.box(11, 2, 2, 16, 14, 14),
        };

        MIDDLE_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
        POINTY_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

        SHAPES = new VoxelShape[6][6];
        SHAPES_RAYCAST = new VoxelShape[6][6];

        SUCKY_AREA = new VoxelShape[6];
        for (var suckyDirection : Direction.values()) {
            var suckyI = suckyDirection.ordinal();

            var infrontSuckyArea =
                    Block.box(0, 0, 0, 16, 16, 16)
                            .move(suckyDirection.getStepX(), suckyDirection.getStepY(), suckyDirection.getStepZ());

            SUCKY_AREA[suckyI] = Shapes.or(insideShapes[suckyI], infrontSuckyArea);

            var mainShapeRaycast = Shapes.or(defaultShapes[suckyI], MIDDLE_SHAPE);
            var mainShape = Shapes.join(
                    mainShapeRaycast, insideShapes[suckyI], BooleanOp.ONLY_FIRST);

            for (var pointyDirection : Direction.values()) {

                var pX = pointyDirection.getStepX() * 0.375;
                var pY = pointyDirection.getStepY() * 0.375;
                var pZ = pointyDirection.getStepZ() * 0.375;
                if (!pointyDirection.getAxis().equals(suckyDirection.getAxis())) {
                    pX += suckyDirection.getStepX() * -0.125;
                    pY += suckyDirection.getStepY() * -0.125;
                    pZ += suckyDirection.getStepZ() * -0.125;
                }
                var pointyShape = POINTY_SHAPE.move(pX, pY, pZ);

                SHAPES[pointyDirection.ordinal()][suckyI] = Shapes.or(mainShape, pointyShape);
                SHAPES_RAYCAST[pointyDirection.ordinal()][suckyI] = Shapes.or(mainShapeRaycast, pointyShape);
            }
        }
    }

    public HopperBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(ENABLED));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        return state == null
               ? null
               : state.setValue(ENABLED, true);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos, Direction direction) {
        if (world.getBlockEntity(pos) instanceof HopperBlockEntity<?> hopperBlockEntity) {
            return StorageUtil.calculateComparatorOutput(hopperBlockEntity.getBehaviour().getStorage());
        }
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return world.isClientSide()
               ? null
               : (world1, pos, state1, blockEntity) -> {
                   if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                       hopperBlockEntity.tick(world, pos, state);
                   }
               };
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && world.getBlockEntity(pos) instanceof HopperBlockEntity<?> hopperBlockEntity) {
            return hopperBlockEntity.onUseWithItem(player, hand, hit);
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        if (!world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                player.openMenu(hopperBlockEntity);
                player.awardStat(Stats.INSPECT_HOPPER);
            }

        }
        return world.isClientSide()
               ? InteractionResult.SUCCESS
               : InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.has(DataComponents.CUSTOM_NAME)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                hopperBlockEntity.setCustomName(itemStack.getHoverName());
            }
        }
    }

    @Override
    protected void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
        this.updateEnabled(world, pos, state);
        super.neighborChanged(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    protected void updateEnabled(Level world, BlockPos pos, BlockState state) {
        boolean bl = !world.hasNeighborSignal(pos);
        if (bl != state.getValue(ENABLED)) {
            world.setBlock(pos, state.setValue(ENABLED, bl), 4);
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.is(state.getBlock())) {
            this.updateEnabled(world, pos, state);
        }
        super.onPlace(state, world, pos, oldState, notify);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel world, BlockPos pos, boolean moved) {
        world.updateNeighbourForOutputSignal(pos, this);

        if (!state.is(world.getBlockState(pos).getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                if (hopperBlockEntity.getBehaviour() instanceof ItemHopperBehaviour itemBehaviour) {
                    Containers.dropContents(world, pos, itemBehaviour.inventory);
                }
            }
        }

        super.affectNeighborsAfterRemoval(state, world, pos, moved);
    }

    protected abstract void buildHopperBlockStateModel(BlockModelGenerators blockStateModelGenerator);

    @Override
    @Environment(EnvType.CLIENT)
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for (var direction : Direction.values()) {
            var suffix = "_" + direction.getName();

            blockStateModelGenerator.createSuffixedVariant(
                    this, suffix,
                    new ModelTemplate(
                            Optional.of(OmniHopper.id("block/hopper" + suffix)),
                            Optional.empty(),
                            TextureSlot.PARTICLE, TextureSlot.SIDE,
                            TextureSlot.TOP, TextureSlot.INSIDE, TextureSlot.BOTTOM
                    ),
                    id -> getTextureMap()
            );
        }

        buildHopperBlockStateModel(blockStateModelGenerator);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void generateItemModel(ItemModelGenerators itemModelGenerator, BlockItem item) {
        itemModelGenerator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }
}
