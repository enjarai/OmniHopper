package nl.enjarai.omnihopper;

import org.slf4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import nl.enjarai.omnihopper.screen.ModScreenHandlers;

public class OmniHopper implements ModInitializer {
	public static final String MODID = "omnihopper";
	public static final Logger LOGGER = ProperLogger.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
		ModScreenHandlers.register();
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
