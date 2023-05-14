package nl.enjarai.omnihopper.util;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface HasTooltip {
    default void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, Identifier id) {
        tooltip.add(Text.translatable(Util.createTranslationKey("item", modifyTooltipId(id)) + ".tooltip")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }

    default Identifier modifyTooltipId(Identifier id) {
        return id;
    }
}
