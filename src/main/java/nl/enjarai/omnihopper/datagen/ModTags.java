package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.DatagenBlock;

import java.util.concurrent.CompletableFuture;

public class ModTags extends FabricTagProvider.BlockTagProvider {
	public ModTags(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider arg) {
		for (var block : ModBlocks.ALL) {
			if (block instanceof DatagenBlock datagen) {
				for (var tagKey : datagen.getConfiguredTags()) {
					var tag = valueLookupBuilder(tagKey);
					tag.add(block);
				}
			}
		}
	}
}
