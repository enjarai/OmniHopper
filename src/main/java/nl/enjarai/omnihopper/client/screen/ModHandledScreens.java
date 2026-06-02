package nl.enjarai.omnihopper.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import nl.enjarai.omnihopper.screen.ModScreenHandlers;

@Environment(EnvType.CLIENT)
public class ModHandledScreens {
	public static void register() {
		MenuScreens.register(ModScreenHandlers.ONE_SLOT_HOPPER, OneSlotHopperScreen::new);
	}
}
