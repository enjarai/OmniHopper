package nl.enjarai.omnihopper;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import nl.enjarai.omnihopper.screen.ModScreenHandlers;
import org.slf4j.Logger;

public class OmniHopper implements ModInitializer, CicadaEntrypoint {
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

	@Override
	public void registerConversations(ConversationManager conversationManager) {
		conversationManager.registerSource(
				JsonSource.fromUrl("https://raw.githubusercontent.com/enjarai/OmniHopper/1.20.2/dev/src/main/resources/cicada/omnihopper/conversations.json")
						.or(JsonSource.fromResource("cicada/omnihopper/conversations.json")),
				LOGGER::info
		);
	}
}
