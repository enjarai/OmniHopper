package nl.enjarai.omnihopper.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import nl.enjarai.omnihopper.OmniHopper;

public class ModScreenHandlers {
	public static final ScreenHandlerType<OneSlotHopperScreenHandler> ONE_SLOT_HOPPER =
			new ScreenHandlerType<>(OneSlotHopperScreenHandler::new, FeatureFlags.VANILLA_FEATURES);

	public static void register() {
		Registry.register(Registries.SCREEN_HANDLER, OmniHopper.id("one_slot_hopper"), ONE_SLOT_HOPPER);
	}
}
