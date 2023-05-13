package nl.enjarai.omnihopper.items;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.HasTooltip;
import org.jetbrains.annotations.Nullable;

public class ModItems {
    public static final List<Item> ALL = new ArrayList<>();
    public static final List<BlockItem> HOPPERS = ModBlocks.ALL.stream().map(ModItems::registerBlockItem).toList();

    public static void register() {}

    private static BlockItem registerBlockItem(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        var item = Registry.register(Registries.ITEM, id, new BlockItem(block, new FabricItemSettings()) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                if (block instanceof HasTooltip hasTooltip) {
                    hasTooltip.appendTooltip(stack, world, tooltip, context, id);
                }

                super.appendTooltip(stack, world, tooltip, context);
            }
        });

        ALL.add(item);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((entries) -> entries.add(item));

        return item;
    }
}
