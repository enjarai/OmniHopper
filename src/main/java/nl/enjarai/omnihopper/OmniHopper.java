package nl.enjarai.omnihopper;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;
import net.minecraft.world.rule.GameRuleType;
import net.minecraft.world.rule.GameRuleVisitor;
import net.minecraft.world.rule.GameRules.Acceptor;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import nl.enjarai.omnihopper.screen.ModScreenHandlers;
import org.slf4j.Logger;

import java.util.function.ToIntFunction;

public class OmniHopper implements ModInitializer, CicadaEntrypoint {
	public static final String MODID = "omnihopper";
	public static final Logger LOGGER = ProperLogger.getLogger(MODID);
    public static final GameRule<Boolean> REMOVE_FURNACE_EXCEPTIONS = registerBooleanRule(
            "remove_furnace_extraction_exceptions",
            GameRuleCategory.MISC,
            true
    );

	@Override
	public void onInitialize() {
		ModBlocks.register();
		ModItems.register();
		ModScreenHandlers.register();
	}

	public static Identifier id(String path) {
		return Identifier.of(MODID, path);
	}

	@Override
	public void registerConversations(ConversationManager conversationManager) {
		conversationManager.registerSource(
				JsonSource.fromUrl("https://raw.githubusercontent.com/enjarai/OmniHopper/1.21/dev/src/main/resources/cicada/omnihopper/conversations.json")
						.or(JsonSource.fromResource("cicada/omnihopper/conversations.json")),
				LOGGER::info
		);
	}

    private static GameRule<Boolean> registerBooleanRule(String name, GameRuleCategory category, boolean defaultValue) {
        return register(name, category, GameRuleType.BOOL, BoolArgumentType.bool(), Codec.BOOL, defaultValue, FeatureSet.empty(), GameRuleVisitor::visitBoolean, (value) -> value ? 1 : 0);
    }

    private static <T> GameRule<T> register(String name, GameRuleCategory category, GameRuleType type, ArgumentType<T> argumentType, Codec<T> codec, T defaultValue, FeatureSet requiredFeatures, Acceptor<T> acceptor, ToIntFunction<T> commandResultSupplier) {
        return  Registry.register(Registries.GAME_RULE, id(name), new GameRule<>(category, type, argumentType, acceptor, codec, commandResultSupplier, defaultValue, requiredFeatures));
    }
}
