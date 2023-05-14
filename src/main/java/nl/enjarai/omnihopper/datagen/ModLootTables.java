package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.server.BlockLootTableGenerator;
import nl.enjarai.omnihopper.blocks.ModBlocks;

public class ModLootTables extends FabricBlockLootTableProvider {
	protected ModLootTables(FabricDataGenerator dataOutput) {
		super(dataOutput);
	}

	@Override
	protected void generateBlockLootTables() {
		ModBlocks.ALL.forEach(block -> addDrop(block, BlockLootTableGenerator::nameableContainerDrops));
	}
}
