package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.OmniHopper;

@SuppressWarnings("UnstableApiUsage")
public class ModBlocks {
    public static final Block OMNIHOPPER_BLOCK = new ItemOmniHopperBlock(FabricBlockSettings
            .of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(3.0F, 4.8F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque()
    );
    public static final BlockEntityType<ItemOmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(ItemOmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build(null);

    public static void register() {
        Registry.register(Registry.BLOCK, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);

        ItemStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> blockEntity.getStorage(), OMNIHOPPER_BLOCK_ENTITY);
    }
}
