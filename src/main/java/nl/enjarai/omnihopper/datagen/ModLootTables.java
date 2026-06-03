package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import nl.enjarai.omnihopper.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModLootTables extends FabricBlockLootSubProvider {
	protected ModLootTables(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
	}

	@Override
	public void generate() {
		ModBlocks.ALL.forEach(block -> add(block, this::createNameableBlockEntityTable));
	}
}
