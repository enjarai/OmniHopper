package nl.enjarai.omnihopper.blocks;

import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.*;
import nl.enjarai.omnihopper.blocks.hopper.*;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocks {
    public static final FabricBlockSettings HOPPER_SETTINGS = FabricBlockSettings
            .of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(3.0F, 4.8F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque();
    public static final FabricBlockSettings WOODEN_HOPPER_SETTINGS = FabricBlockSettings
            .of(Material.WOOD, MapColor.BROWN)
            .requiresTool()
            .strength(2.0f, 3.0f)
            .sounds(BlockSoundGroup.WOOD)
            .nonOpaque();

    // Item Omnihopper
    public static final Block OMNIHOPPER_BLOCK = register("omnihopper", new ItemOmniHopperBlock(HOPPER_SETTINGS));
    public static final BlockEntityType<ItemOmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(ItemOmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build(null);

    // Fluid Omnihopper
    public static final Block FLUID_OMNIHOPPER_BLOCK = register("fluid_omnihopper", new OxidizableFluidOmniHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_EXPOSED = register("exposed_fluid_omnihopper", new OxidizableFluidOmniHopperBlock(Oxidizable.OxidationLevel.EXPOSED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_WEATHERED = register("weathered_fluid_omnihopper", new OxidizableFluidOmniHopperBlock(Oxidizable.OxidationLevel.WEATHERED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_OXIDIZED = register("oxidized_fluid_omnihopper", new OxidizableFluidOmniHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED = register("waxed_fluid_omnihopper", new FluidOmniHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED = register("waxed_exposed_fluid_omnihopper", new FluidOmniHopperBlock(Oxidizable.OxidationLevel.EXPOSED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED = register("waxed_weathered_fluid_omnihopper", new FluidOmniHopperBlock(Oxidizable.OxidationLevel.WEATHERED, HOPPER_SETTINGS));
    public static final Block FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED = register("waxed_oxidized_fluid_omnihopper", new FluidOmniHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, HOPPER_SETTINGS));
    public static final BlockEntityType<FluidOmniHopperBlockEntity> FLUID_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidOmniHopperBlockEntity::new,
                    FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_OXIDIZED,
                    FLUID_OMNIHOPPER_BLOCK_WAXED, FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED, FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED
            ).build(null);

    // Basic Fluid Hopper
    public static final Block FLUID_HOPPER_BLOCK = register("fluid_hopper", new OxidizableFluidHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_EXPOSED = register("exposed_fluid_hopper", new OxidizableFluidHopperBlock(Oxidizable.OxidationLevel.EXPOSED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_WEATHERED = register("weathered_fluid_hopper", new OxidizableFluidHopperBlock(Oxidizable.OxidationLevel.WEATHERED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_OXIDIZED = register("oxidized_fluid_hopper", new OxidizableFluidHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_WAXED = register("waxed_fluid_hopper", new FluidHopperBlock(Oxidizable.OxidationLevel.UNAFFECTED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_WAXED_EXPOSED = register("waxed_exposed_fluid_hopper", new FluidHopperBlock(Oxidizable.OxidationLevel.EXPOSED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_WAXED_WEATHERED = register("waxed_weathered_fluid_hopper", new FluidHopperBlock(Oxidizable.OxidationLevel.WEATHERED, HOPPER_SETTINGS));
    public static final Block FLUID_HOPPER_BLOCK_WAXED_OXIDIZED = register("waxed_oxidized_fluid_hopper", new FluidHopperBlock(Oxidizable.OxidationLevel.OXIDIZED, HOPPER_SETTINGS));
    public static final BlockEntityType<FluidHopperBlockEntity> FLUID_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidHopperBlockEntity::new,
                    FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_OXIDIZED,
                    FLUID_HOPPER_BLOCK_WAXED, FLUID_HOPPER_BLOCK_WAXED_EXPOSED, FLUID_HOPPER_BLOCK_WAXED_WEATHERED, FLUID_HOPPER_BLOCK_WAXED_OXIDIZED
            ).build(null);

    // Wooden OmniHopper
    public static final Block WOODEN_OMNIHOPPER_BLOCK = register("wooden_omnihopper", new WoodenOmniHopperBlock(WOODEN_HOPPER_SETTINGS));
    public static final BlockEntityType<WoodenOmniHopperBlockEntity> WOODEN_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(WoodenOmniHopperBlockEntity::new, WOODEN_OMNIHOPPER_BLOCK).build(null);

    // Basic Wooden Hopper
    public static final Block WOODEN_HOPPER_BLOCK = register("wooden_hopper", new WoodenHopperBlock(WOODEN_HOPPER_SETTINGS));
    public static final BlockEntityType<WoodenHopperBlockEntity> WOODEN_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(WoodenHopperBlockEntity::new, WOODEN_HOPPER_BLOCK).build(null);

    public static final List<Block> ALL = List.of(
            OMNIHOPPER_BLOCK,
            FLUID_OMNIHOPPER_BLOCK, FLUID_OMNIHOPPER_BLOCK_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WEATHERED, FLUID_OMNIHOPPER_BLOCK_OXIDIZED,
            FLUID_OMNIHOPPER_BLOCK_WAXED, FLUID_OMNIHOPPER_BLOCK_WAXED_EXPOSED, FLUID_OMNIHOPPER_BLOCK_WAXED_WEATHERED, FLUID_OMNIHOPPER_BLOCK_WAXED_OXIDIZED,
            FLUID_HOPPER_BLOCK, FLUID_HOPPER_BLOCK_EXPOSED, FLUID_HOPPER_BLOCK_WEATHERED, FLUID_HOPPER_BLOCK_OXIDIZED,
            FLUID_HOPPER_BLOCK_WAXED, FLUID_HOPPER_BLOCK_WAXED_EXPOSED, FLUID_HOPPER_BLOCK_WAXED_WEATHERED, FLUID_HOPPER_BLOCK_WAXED_OXIDIZED,
            WOODEN_OMNIHOPPER_BLOCK,
            WOODEN_HOPPER_BLOCK
    );

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, OmniHopper.id(name), block);
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

        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("wooden_omnihopper"), WOODEN_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("wooden_hopper"), WOODEN_HOPPER_BLOCK_ENTITY);

        // Register block entities with the Transfer API
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_HOPPER_BLOCK_ENTITY);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), WOODEN_OMNIHOPPER_BLOCK_ENTITY);
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), WOODEN_HOPPER_BLOCK_ENTITY);
    }
}
