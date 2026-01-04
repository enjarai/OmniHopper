package nl.enjarai.omnihopper.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Model;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

import java.util.Optional;
import java.util.Set;

public interface DatagenBlock {
    @Environment(EnvType.CLIENT)
    default void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    default Set<TagKey<Block>> getConfiguredTags() {
        return Set.of();
    }

    @Environment(EnvType.CLIENT)
    default void generateItemModel(ItemModelGenerator itemModelGenerator, BlockItem item) {
        var id = Registries.ITEM.getId(item);
        itemModelGenerator.register(item, new Model(Optional.of(id.withPath(path -> "block/" + path)), Optional.empty()));
    }
}
