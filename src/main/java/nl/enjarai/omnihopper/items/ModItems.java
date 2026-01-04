package nl.enjarai.omnihopper.items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.HasTooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ModItems {
    public static final List<Item> ALL = new ArrayList<>();
    public static final List<BlockItem> HOPPERS = ModBlocks.ALL.stream().map(ModItems::registerBlockItem).toList();

    public static void register() {}

    private static BlockItem registerBlockItem(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        var item = Registry.register(Registries.ITEM, id, new BlockItem(block, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id)).useBlockPrefixedTranslationKey()) {
            @Override
            public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
                if (block instanceof HasTooltip hasTooltip) {
                    hasTooltip.appendTooltip(stack, context, textConsumer, type, id);
                }

                super.appendTooltip(stack, context, displayComponent, textConsumer, type);
            }
        });

        ALL.add(item);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register((entries) -> entries.add(item));

        return item;
    }
}
