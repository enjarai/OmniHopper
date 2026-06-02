package nl.enjarai.omnihopper.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import java.util.function.Consumer;

public interface HasTooltip {
    default void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type, Identifier id) {
        tooltip.accept(Component.translatable(Util.makeDescriptionId("item", modifyTooltipId(id)) + ".tooltip")
                .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
    }

    default Identifier modifyTooltipId(Identifier id) {
        return id;
    }
}
