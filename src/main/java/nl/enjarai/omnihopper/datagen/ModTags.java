package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.util.registry.Registry;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.util.DatagenBlock;

public class ModTags extends FabricTagProvider<Block> {
	public ModTags(FabricDataGenerator output) {
		super(output, Registry.BLOCK);
	}

	@Override
	protected void generateTags() {
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
