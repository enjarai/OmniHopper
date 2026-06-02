package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.OpenBoxBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.hopper.*;
import nl.enjarai.omnihopper.blocks.hopper.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<BlockBehaviour.Properties> HOPPER_SETTINGS = () -> BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.STONE)
            .requiresCorrectToolForDrops()
            .strength(3.0F, 4.8F)
            .sound(SoundType.METAL)
            .noOcclusion();
    public static final Supplier<BlockBehaviour.Properties> WOODEN_HOPPER_SETTINGS = () -> BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.COLOR_BROWN)
            .requiresCorrectToolForDrops()
            .strength(2.0f, 3.0f)
            .sound(SoundType.WOOD)
            .noOcclusion();

    // Item Omnihopper
    public static final Block OMNIHOPPER_BLOCK = register("omnihopper", ItemOmniHopperBlock::new, HOPPER_SETTINGS);
    public static final BlockEntityType<ItemOmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(ItemOmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build();

    // Basic Fluid Hopper
    public static final Block FLUID_HOPPER_BLOCK = register("fluid_hopper", settings -> new OxidizableFluidHopperBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_EXPOSED = register("exposed_fluid_hopper", settings -> new OxidizableFluidHopperBlock(WeatheringCopper.WeatherState.EXPOSED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_WEATHERED = register("weathered_fluid_hopper", settings -> new OxidizableFluidHopperBlock(WeatheringCopper.WeatherState.WEATHERED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_OXIDIZED = register("oxidized_fluid_hopper", settings -> new OxidizableFluidHopperBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_WAXED = register("waxed_fluid_hopper", settings -> new FluidHopperBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_WAXED_EXPOSED = register("waxed_exposed_fluid_hopper", settings -> new FluidHopperBlock(WeatheringCopper.WeatherState.EXPOSED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_WAXED_WEATHERED = register("waxed_weathered_fluid_hopper", settings -> new FluidHopperBlock(WeatheringCopper.WeatherState.WEATHERED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_HOPPER_BLOCK_WAXED_OXIDIZED = register("waxed_oxidized_fluid_hopper", settings -> new FluidHopperBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), HOPPER_SETTINGS);
    public static final BlockEntityType<FluidHopperBlockEntity> FLUID_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidHopperBlockEntity::new,
                    FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_OXIDIZED,
                    FLUID_HOPPER_BLOCK_WAXED, FLUID_HOPPER_BLOCK_WAXED_EXPOSED, FLUID_HOPPER_BLOCK_WAXED_WEATHERED, FLUID_HOPPER_BLOCK_WAXED_OXIDIZED
            ).build();

    // Fluid Omnihopper
    public static final Block FLUID_OMNIHOPPER_BLOCK = register("fluid_omnihopper", settings -> new OxidizableFluidOmniHopperBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_EXPOSED = register("exposed_fluid_omnihopper", settings -> new OxidizableFluidOmniHopperBlock(WeatheringCopper.WeatherState.EXPOSED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_WEATHERED = register("weathered_fluid_omnihopper", settings -> new OxidizableFluidOmniHopperBlock(WeatheringCopper.WeatherState.WEATHERED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_OXIDIZED = register("oxidized_fluid_omnihopper", settings -> new OxidizableFluidOmniHopperBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED = register("waxed_fluid_omnihopper", settings -> new FluidOmniHopperBlock(WeatheringCopper.WeatherState.UNAFFECTED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED = register("waxed_exposed_fluid_omnihopper", settings -> new FluidOmniHopperBlock(WeatheringCopper.WeatherState.EXPOSED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED = register("waxed_weathered_fluid_omnihopper", settings -> new FluidOmniHopperBlock(WeatheringCopper.WeatherState.WEATHERED, settings), HOPPER_SETTINGS);
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED = register("waxed_oxidized_fluid_omnihopper", settings -> new FluidOmniHopperBlock(WeatheringCopper.WeatherState.OXIDIZED, settings), HOPPER_SETTINGS);
    public static final BlockEntityType<FluidOmniHopperBlockEntity> FLUID_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidOmniHopperBlockEntity::new,
                    FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_OXIDIZED,
                    FLUID_OMNIHOPPER_BLOCK_WAXED, FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED, FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED
            ).build();

    // Basic Wooden Hopper
    public static final Block WOODEN_HOPPER_BLOCK = register("wooden_hopper", WoodenHopperBlock::new, WOODEN_HOPPER_SETTINGS);
    public static final BlockEntityType<WoodenHopperBlockEntity> WOODEN_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(WoodenHopperBlockEntity::new, WOODEN_HOPPER_BLOCK).build();

    // Wooden OmniHopper
    public static final Block WOODEN_OMNIHOPPER_BLOCK = register("wooden_omnihopper", WoodenOmniHopperBlock::new, WOODEN_HOPPER_SETTINGS);
    public static final BlockEntityType<WoodenOmniHopperBlockEntity> WOODEN_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(WoodenOmniHopperBlockEntity::new, WOODEN_OMNIHOPPER_BLOCK).build();

    // Open Box Block
    public static final Block OPEN_BOX_BLOCK = register("open_box", OpenBoxBlock::new, WOODEN_HOPPER_SETTINGS);
    public static final BlockEntityType<OpenBoxBlockEntity> OPEN_BOX_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(OpenBoxBlockEntity::new, OPEN_BOX_BLOCK).build();

    public static final List<Block> ALL = List.of(
            OMNIHOPPER_BLOCK,
            FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_OXIDIZED,
            FLUID_HOPPER_BLOCK_WAXED, FLUID_HOPPER_BLOCK_WAXED_EXPOSED, FLUID_HOPPER_BLOCK_WAXED_WEATHERED, FLUID_HOPPER_BLOCK_WAXED_OXIDIZED,
            FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_OXIDIZED,
            FLUID_OMNIHOPPER_BLOCK_WAXED, FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED, FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED,
            WOODEN_HOPPER_BLOCK,
            WOODEN_OMNIHOPPER_BLOCK,

            OPEN_BOX_BLOCK
    );

    private static Block register(String name, Function<BlockBehaviour.Properties, Block> block, Supplier<BlockBehaviour.Properties> settings) {
        return Registry.register(BuiltInRegistries.BLOCK, OmniHopper.id(name), block.apply(settings.get().setId(ResourceKey.create(Registries.BLOCK, OmniHopper.id(name)))));
    }

    public static void register() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_EXPOSED);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WEATHERED);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_OXIDIZED);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_EXPOSED);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WEATHERED);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_OXIDIZED);

        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_WAXED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WAXED_EXPOSED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_WAXED_WEATHERED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_HOPPER_BLOCK_OXIDIZED, FLUID_HOPPER_BLOCK_WAXED_OXIDIZED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_WAXED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED);
        OxidizableBlocksRegistry.registerWaxableBlockPair(FLUID_OMNIHOPPER_BLOCK_OXIDIZED, FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED);

        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("wooden_omnihopper"), WOODEN_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("wooden_hopper"), WOODEN_HOPPER_BLOCK_ENTITY);

        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, OmniHopper.id("open_box"), OPEN_BOX_BLOCK_ENTITY);

        // Register block entities with the Transfer API
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_HOPPER_BLOCK_ENTITY);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), WOODEN_OMNIHOPPER_BLOCK_ENTITY);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), WOODEN_HOPPER_BLOCK_ENTITY);

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getItemStorage(), OPEN_BOX_BLOCK_ENTITY);
    }
}
