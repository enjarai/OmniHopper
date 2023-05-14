package nl.enjarai.omnihopper.util;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.item.BlockItem;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.Set;

public interface DatagenBlock {
    default void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    default Set<TagKey<Block>> getConfiguredTags() {
        return Set.of();
    }

    default void generateItemModel(ItemModelGenerator itemModelGenerator, BlockItem item) {
        var id = Registry.ITEM.getId(item);
        itemModelGenerator.register(item, new Model(Optional.of(new Identifier(id.getNamespace(), "block/" + id.getPath())), Optional.empty()));
    }
}
