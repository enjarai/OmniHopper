package nl.enjarai.omnihopper.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.OmniHopper;

public class ModBlocks {
    public static final Block OMNIHOPPER_BLOCK = new OmniHopperBlock(FabricBlockSettings
            .of(Material.METAL, MapColor.STONE_GRAY)
            .requiresTool()
            .strength(3.0F, 4.8F)
            .sounds(BlockSoundGroup.METAL)
            .nonOpaque()
    );
    public static final BlockEntityType<OmniHopperBlockEntity> OMNIHOPPER_BLOCK_ENTITY =
            FabricBlockEntityTypeBuilder.create(OmniHopperBlockEntity::new, OMNIHOPPER_BLOCK).build(null);

    public static void register() {
        Registry.register(Registry.BLOCK, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK);

        Registry.register(Registry.BLOCK_ENTITY_TYPE, OmniHopper.id("omnihopper"), OMNIHOPPER_BLOCK_ENTITY);
    }
}
