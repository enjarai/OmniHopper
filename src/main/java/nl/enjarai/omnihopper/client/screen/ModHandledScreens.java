package nl.enjarai.omnihopper.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import nl.enjarai.omnihopper.screen.ModScreenHandlers;

@Environment(EnvType.CLIENT)
public class ModHandledScreens {
	public static void register() {
		HandledScreens.register(ModScreenHandlers.ONE_SLOT_HOPPER, OneSlotHopperScreen::new);
	}
}
