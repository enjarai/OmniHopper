package nl.enjarai.omnihopper.blocks;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.BlockSoundGroup;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.FluidHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.FluidOmniHopperBlockEntity;
import nl.enjarai.omnihopper.blocks.entity.ItemOmniHopperBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocks {
    public static final FabricBlockSettings OMNIHOPPER_BLOCK_SETTINGS = FabricBlockSettings
            .of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(3.0F, 4.8F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque();

    // Item Omnihopper
    public static final Block OMNIHOPPER_BLOCK = new ItemOmniHopperBlock(OMNIHOPPER_BLOCK_SETTINGS);
    public static final BlockEntityType<ItemOmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(ItemOmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build(null);

    // Fluid Omnihopper
    public static final Block FLUID_OMNIHOPPER_BLOCK = new FluidOmniHopperBlock(OMNIHOPPER_BLOCK_SETTINGS);
    public static final BlockEntityType<FluidOmniHopperBlockEntity> FLUID_OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidOmniHopperBlockEntity::new, FLUID_OMNIHOPPER_BLOCK).build(null);

    // Basic Fluid Hopper
    public static final Block FLUID_HOPPER_BLOCK = new FluidHopperBlock(OMNIHOPPER_BLOCK_SETTINGS);
    public static final BlockEntityType<FluidHopperBlockEntity> FLUID_HOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(FluidHopperBlockEntity::new, FLUID_HOPPER_BLOCK).build(null);

    public static final List<Block> ALL = List.of(
            OMNIHOPPER_BLOCK,
            FLUID_OMNIHOPPER_BLOCK,
            FLUID_HOPPER_BLOCK
    );

    public static void register() {
        Registry.register(Registries.BLOCK, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK);
        Registry.register(Registries.BLOCK, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK);
        Registry.register(Registries.BLOCK, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK);

        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_omnihopper"), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        Registry.register(Registries.BLOCK_ENTITY_TYPE, OmniHopper.id("fluid_hopper"), FLUID_HOPPER_BLOCK_ENTITY);

        // Register block entities with the Transfer API
        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_OMNIHOPPER_BLOCK_ENTITY);
        FluidStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getBehaviour().getStorage(), FLUID_HOPPER_BLOCK_ENTITY);
    }
}
