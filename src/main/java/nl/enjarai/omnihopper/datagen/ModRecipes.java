package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import nl.enjarai.omnihopper.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipes extends FabricRecipeProvider {
    public ModRecipes(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {
            @Override
            public void generate() {
                var items = registryLookup.getOrThrow(RegistryKeys.ITEM);
                ShapedRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.FLUID_HOPPER_BLOCK)
                        .pattern("c c")
                        .pattern("cBc")
                        .pattern(" c ")
                        .input('B', Items.BUCKET)
                        .input('c', Items.COPPER_INGOT)
                        .criterion("has_copper_ingot", conditionsFromItem(Items.COPPER_INGOT))
                        .criterion("has_bucket", conditionsFromItem(Items.BUCKET))
                        .offerTo(exporter);
                ShapelessRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.FLUID_OMNIHOPPER_BLOCK)
                        .input(ModBlocks.FLUID_HOPPER_BLOCK.asItem())
                        .input(Items.COPPER_INGOT)
                        .criterion("has_fluid_hopper", conditionsFromItem(ModBlocks.FLUID_HOPPER_BLOCK.asItem()))
                        .criterion("has_copper_ingot", conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
                ShapedRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.OPEN_BOX_BLOCK)
                        .pattern("w w")
                        .pattern("w w")
                        .pattern("www")
                        .input('w', ItemTags.PLANKS)
                        .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
                        .offerTo(exporter);
                ShapelessRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.OMNIHOPPER_BLOCK)
                        .input(Items.HOPPER)
                        .input(Items.COPPER_INGOT)
                        .criterion("has_hopper", conditionsFromItem(Items.HOPPER))
                        .criterion("has_copper_ingot", conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
                ShapedRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.WOODEN_HOPPER_BLOCK)
                        .pattern("w w")
                        .pattern("wCw")
                        .pattern(" w ")
                        .input('C', Items.CHEST)
                        .input('w', ItemTags.PLANKS)
                        .criterion("has_chest", conditionsFromItem(Items.CHEST))
                        .criterion("has_planks", conditionsFromTag(ItemTags.PLANKS))
                        .offerTo(exporter);
                ShapelessRecipeJsonBuilder.create(items, RecipeCategory.REDSTONE, ModBlocks.WOODEN_OMNIHOPPER_BLOCK)
                        .input(ModBlocks.WOODEN_HOPPER_BLOCK.asItem())
                        .input(Items.COPPER_INGOT)
                        .criterion("has_wooden_hopper", conditionsFromItem(ModBlocks.WOODEN_HOPPER_BLOCK.asItem()))
                        .criterion("has_copper_ingot", conditionsFromItem(Items.COPPER_INGOT))
                        .offerTo(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipes";
    }
}
