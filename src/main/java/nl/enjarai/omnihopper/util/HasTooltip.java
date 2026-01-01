package nl.enjarai.omnihopper.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Consumer;

public interface HasTooltip {
    default void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, Identifier id) {
        tooltip.accept(Text.translatable(Util.createTranslationKey("item", modifyTooltipId(id)) + ".tooltip")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }

    default Identifier modifyTooltipId(Identifier id) {
        return id;
    }
}
