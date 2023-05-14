package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ModDatagen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.addProvider(ModLootTables::new);
		fabricDataGenerator.addProvider(ModTags::new);
		fabricDataGenerator.addProvider(ModModels::new);
	}
}
