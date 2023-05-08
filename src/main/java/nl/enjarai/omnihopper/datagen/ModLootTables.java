package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import nl.enjarai.omnihopper.blocks.ModBlocks;

public class ModLootTables extends FabricBlockLootTableProvider {
	protected ModLootTables(FabricDataOutput dataOutput) {
		super(dataOutput);
	}

	@Override
	public void generate() {
		ModBlocks.ALL.forEach(block -> addDrop(block, this::nameableContainerDrops));
	}
}
