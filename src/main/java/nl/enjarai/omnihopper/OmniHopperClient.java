package nl.enjarai.omnihopper;

import net.fabricmc.api.ClientModInitializer;
import nl.enjarai.omnihopper.client.screen.ModHandledScreens;

public class OmniHopperClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModHandledScreens.register();
	}
}
