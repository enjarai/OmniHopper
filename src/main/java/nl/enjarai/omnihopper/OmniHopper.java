package nl.enjarai.omnihopper;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OmniHopper implements ModInitializer {
	public static final String MODID = "omnihopper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
	}

	public static Identifier id(String path) {
		return new Identifier(MODID, path);
	}
}
