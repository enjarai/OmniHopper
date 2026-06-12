package nl.enjarai.omnihopper.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import java.util.Optional;
import java.util.Set;

public interface DatagenBlock {
    @Environment(EnvType.CLIENT)
    default void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    }

    default Set<TagKey<Block>> getConfiguredTags() {
        return Set.of();
    }

    @Environment(EnvType.CLIENT)
    default void generateItemModel(ItemModelGenerators itemModelGenerator, BlockItem item) {
        var id = BuiltInRegistries.ITEM.getKey(item);
        itemModelGenerator.generateFlatItem(item, new ModelTemplate(Optional.of(id.withPath(path -> "block/" + path)), Optional.empty()));
    }
}
