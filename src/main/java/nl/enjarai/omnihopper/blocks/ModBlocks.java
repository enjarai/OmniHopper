package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.OmniHopper;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocks {
    public static final FabricBlockSettings OMNIHOPPER_BLOCK_SETTINGS = FabricBlockSettings
            .of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(3.0F, 4.8F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque();

    // Normal Omnihopper
    public static final Block OMNIHOPPER_BLOCK = new ItemOmniHopperBlock(OMNIHOPPER_BLOCK_SETTINGS);
    public static final BlockEntityType<ItemOmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(ItemOmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build(null);

    // Normal fluid Omnihopper
    public static final Block FLUID_OMNIHOPPER_BLOCK = new FluidOmniHopperBlock(OMNIHOPPER_BLOCK_SETTINGS);
    public static final BlockEntityType<FluidOmniHopperBlockEntity> FLUID_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidOmniHopperBlockEntity::new, FLUID_OMNIHOPPER_BLOCK).build(null);

    // Hacky workaround for creating a normal fluid hopper, mostly for consistency
    public static final Block FLUID_HOPPER_BLOCK = new FluidOmniHopperBlock(OMNIHOPPER_BLOCK_SETTINGS) {
        @Override
        public BlockState getPlacementState(ItemPlacementContext ctx) {
            Direction direction = ctx.getSide().getOpposite();
            return this.getDefaultState()
                    .with(SUCKY_BIT, Direction.UP)
                    .with(POINTY_BIT, direction.getAxis() == Direction.Axis.Y ? Direction.DOWN : direction)
                    .with(ENABLED, true);
        }
    };
    public static final BlockEntityType<FluidOmniHopperBlockEntity> FLUID_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidOmniHopperBlockEntity::new, FLUID_HOPPER_BLOCK).build(null);


    public static void register() {
        Registry.register(Registry.BLOCK, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK);
        Registry.register(Registry.BLOCK, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK);
        Registry.register(Registry.BLOCK, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK_ENTITY);

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getStorage(), OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getStorage(), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getStorage(), FLUID_HOPPER_BLOCK_ENTITY);
    }
}
