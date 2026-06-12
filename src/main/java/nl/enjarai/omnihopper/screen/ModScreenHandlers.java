package nl.enjarai.omnihopper.screen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import nl.enjarai.omnihopper.OmniHopper;

public class ModScreenHandlers {
	public static final MenuType<OneSlotHopperScreenHandler> ONE_SLOT_HOPPER =
			new MenuType<>(OneSlotHopperScreenHandler::new, FeatureFlags.VANILLA_SET);

	public static void register() {
		Registry.register(BuiltInRegistries.MENU, OmniHopper.id("one_slot_hopper"), ONE_SLOT_HOPPER);
	}
}
