package nl.enjarai.omnihopper.datagen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.DatagenBlock;

public class ModTags extends FabricTagProvider<Block> {
	public ModTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, Registries.BLOCK.getKey(), registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		for (var block : ModBlocks.ALL) {
			if (block instanceof DatagenBlock datagen) {
				for (var tagKey : datagen.getConfiguredTags()) {
					var tag = getOrCreateTagBuilder(tagKey);
					tag.add(block);
				}
			}
		}
	}
}
