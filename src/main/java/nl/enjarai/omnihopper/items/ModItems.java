package nl.enjarai.omnihopper.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.HasTooltip;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<Item> ALL = new ArrayList<>();
    public static final List<BlockItem> HOPPERS = ModBlocks.ALL.stream().map(ModItems::registerBlockItem).toList();

    public static void register() {}

    private static BlockItem registerBlockItem(Block block) {
        Identifier id = Registry.BLOCK.getId(block);
        var item = Registry.register(Registry.ITEM, id, new BlockItem(block, new FabricItemSettings().group(ItemGroup.REDSTONE)) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                if (block instanceof HasTooltip hasTooltip) {
                    hasTooltip.appendTooltip(stack, world, tooltip, context, id);
                }

                super.appendTooltip(stack, world, tooltip, context);
            }
        });

        ALL.add(item);

        return item;
    }
}
