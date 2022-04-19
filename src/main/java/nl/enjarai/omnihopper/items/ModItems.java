package nl.enjarai.omnihopper.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.blocks.ModBlocks;

public class ModItems {
    public static final Item OMNIHOPPER = registerBlockItem(ModBlocks.OMNIHOPPER_BLOCK);
    public static final Item FLUID_HOPPER = registerBlockItem(ModBlocks.FLUID_HOPPER_BLOCK);
    public static final Item FLUID_OMNIHOPPER = registerBlockItem(ModBlocks.FLUID_OMNIHOPPER_BLOCK);

    public static void register() {}

    private static Item registerBlockItem(Block block) {
        return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block),
                new BlockItem(block, new FabricItemSettings().group(ItemGroup.REDSTONE)));
    }
}
