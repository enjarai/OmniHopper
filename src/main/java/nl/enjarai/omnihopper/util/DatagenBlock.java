package nl.enjarai.omnihopper.util;

import java.util.Optional;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public interface DatagenBlock {
    default void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    default Set<TagKey<Block>> getConfiguredTags() {
        return Set.of();
    }

    default void generateItemModel(ItemModelGenerator itemModelGenerator, BlockItem item) {
        var id = Registries.ITEM.getId(item);
        itemModelGenerator.register(item, new Model(Optional.of(id.withPath(path -> "block/" + path)), Optional.empty()));
    }
}
