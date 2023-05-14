package nl.enjarai.omnihopper.blocks.hopper;

import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.client.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.ItemHopperBehaviour;
import nl.enjarai.omnihopper.util.DatagenBlock;
import nl.enjarai.omnihopper.util.HasTooltip;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public abstract class HopperBlock extends BlockWithEntity implements DatagenBlock, TextureMapProvider, HasTooltip {
    public static final BooleanProperty ENABLED;
    public static final VoxelShape[] SUCKY_AREA;
    private static final VoxelShape MIDDLE_SHAPE;
    private static final VoxelShape POINTY_SHAPE;
    protected static final VoxelShape[][] SHAPES;
    protected static final VoxelShape[][] SHAPES_RAYCAST;

    static {
        ENABLED = Properties.ENABLED;

        var defaultShapes = new VoxelShape[]{
                Block.createCuboidShape(0, 0, 0, 16, 6, 16),
                Block.createCuboidShape(0, 10, 0, 16, 16, 16),

                Block.createCuboidShape(0, 0, 0, 16, 16, 6),
                Block.createCuboidShape(0, 0, 10, 16, 16, 16),

                Block.createCuboidShape(0, 0, 0, 6, 16, 16),
                Block.createCuboidShape(10, 0, 0, 16, 16, 16),
        };
        var insideShapes = new VoxelShape[]{
                Block.createCuboidShape(2, 0, 2, 14, 5, 14),
                Block.createCuboidShape(2, 11, 2, 14, 16, 14),

                Block.createCuboidShape(2, 2, 0, 14, 14, 5),
                Block.createCuboidShape(2, 2, 11, 14, 14, 16),

                Block.createCuboidShape(0, 2, 2, 5, 14, 14),
                Block.createCuboidShape(11, 2, 2, 16, 14, 14),
        };

        MIDDLE_SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
        POINTY_SHAPE = Block.createCuboidShape(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

        SHAPES = new VoxelShape[6][6];
        SHAPES_RAYCAST = new VoxelShape[6][6];

        SUCKY_AREA = new VoxelShape[6];
        for (var suckyDirection : Direction.values()) {
            var suckyI = suckyDirection.ordinal();

            var infrontSuckyArea =
                    Block.createCuboidShape(0, 0, 0, 16, 16, 16)
                            .offset(suckyDirection.getOffsetX(), suckyDirection.getOffsetY(), suckyDirection.getOffsetZ());

            SUCKY_AREA[suckyI] = VoxelShapes.union(insideShapes[suckyI], infrontSuckyArea);

            var mainShapeRaycast = VoxelShapes.union(defaultShapes[suckyI], MIDDLE_SHAPE);
            var mainShape = VoxelShapes.combineAndSimplify(
                    mainShapeRaycast, insideShapes[suckyI], BooleanBiFunction.ONLY_FIRST);

            for (var pointyDirection : Direction.values()) {

                var pX = pointyDirection.getOffsetX() * 0.375;
                var pY = pointyDirection.getOffsetY() * 0.375;
                var pZ = pointyDirection.getOffsetZ() * 0.375;
                if (!pointyDirection.getAxis().equals(suckyDirection.getAxis())) {
                    pX += suckyDirection.getOffsetX() * -0.125;
                    pY += suckyDirection.getOffsetY() * -0.125;
                    pZ += suckyDirection.getOffsetZ() * -0.125;
                }
                var pointyShape = POINTY_SHAPE.offset(pX, pY, pZ);

                SHAPES[pointyDirection.ordinal()][suckyI] = VoxelShapes.union(mainShape, pointyShape);
                SHAPES_RAYCAST[pointyDirection.ordinal()][suckyI] = VoxelShapes.union(mainShapeRaycast, pointyShape);
            }
        }
    }

    public HopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder.add(ENABLED));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        return state == null ? null : state.with(ENABLED, true);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof HopperBlockEntity<?> hopperBlockEntity) {
            return StorageUtil.calculateComparatorOutput(hopperBlockEntity.getBehaviour().getStorage());
        }
        return 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : (world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                hopperBlockEntity.tick(world, pos, state);
            }
        };
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                player.openHandledScreen(hopperBlockEntity);
                player.incrementStat(Stats.INSPECT_HOPPER);
            }

            return ActionResult.CONSUME;
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                hopperBlockEntity.setCustomName(itemStack.getName());
            }
        }
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        this.updateEnabled(world, pos, state);
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    protected void updateEnabled(World world, BlockPos pos, BlockState state) {
        boolean bl = !world.isReceivingRedstonePower(pos);
        if (bl != state.get(ENABLED)) {
            world.setBlockState(pos, state.with(ENABLED, bl), 4);
        }
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            this.updateEnabled(world, pos, state);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        world.updateComparators(pos, this);

        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof HopperBlockEntity<?> hopperBlockEntity) {
                if (hopperBlockEntity.getBehaviour() instanceof ItemHopperBehaviour itemBehaviour) {
                    ItemScatterer.spawn(world, pos, itemBehaviour.inventory);
                }
            }
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    protected abstract void buildHopperBlockStateModel(BlockStateModelGenerator blockStateModelGenerator);

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (var direction : Direction.values()) {
            var suffix = "_" + direction.getName();

            blockStateModelGenerator.createSubModel(
                    this, suffix,
                    new Model(
                            Optional.of(OmniHopper.id("block/hopper" + suffix)),
                            Optional.empty(),
                            TextureKey.PARTICLE, TextureKey.SIDE,
                            TextureKey.TOP, TextureKey.INSIDE, TextureKey.BOTTOM
                    ),
                    id -> getTextureMap()
            );
        }

        buildHopperBlockStateModel(blockStateModelGenerator);
    }

    @Override
    public Set<TagKey<Block>> getConfiguredTags() {
        return Set.of(BlockTags.PICKAXE_MINEABLE);
    }

    @Override
    public void generateItemModel(ItemModelGenerator itemModelGenerator, BlockItem item) {
        itemModelGenerator.register(item, Models.GENERATED);
    }
}
