package nl.enjarai.omnihopper.util;

import net.minecraft.data.client.BlockStateModelGenerator;

public interface Datagen {
    default void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }
}
