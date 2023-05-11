package nl.enjarai.omnihopper.util;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.registry.tag.TagKey;

public interface DatagenBlock {
    default void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    default Set<TagKey<Block>> getConfiguredTags() {
        return Set.of();
    }
}
