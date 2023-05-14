package nl.enjarai.omnihopper.screen;

import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.OmniHopper;

public class ModScreenHandlers {
	public static final ScreenHandlerType<OneSlotHopperScreenHandler> ONE_SLOT_HOPPER =
			new ScreenHandlerType<>(OneSlotHopperScreenHandler::new);

	public static void register() {
		Registry.register(Registry.SCREEN_HANDLER, OmniHopper.id("one_slot_hopper"), ONE_SLOT_HOPPER);
	}
}
