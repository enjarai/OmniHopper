package nl.enjarai.omnihopper.items;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import nl.enjarai.omnihopper.blocks.ModBlocks;

public class ModItems {
    public static final List<BlockItem> ALL = new ArrayList<>();

    public static final Item OMNIHOPPER = registerBlockItem(ModBlocks.OMNIHOPPER_BLOCK);
    public static final Item FLUID_HOPPER = registerBlockItem(ModBlocks.FLUID_HOPPER_BLOCK);
    public static final Item FLUID_OMNIHOPPER = registerBlockItem(ModBlocks.FLUID_OMNIHOPPER_BLOCK);


    public static void register() {}

    private static Item registerBlockItem(Block block) {
        var item = Registry.register(Registries.ITEM, Registries.BLOCK.getId(block),
                new BlockItem(block, new FabricItemSettings()));

        ALL.add(item);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((entries) -> entries.add(item));

        return item;
    }
}
