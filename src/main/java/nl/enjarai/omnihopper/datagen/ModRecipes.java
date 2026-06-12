package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import nl.enjarai.omnihopper.blocks.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipes extends FabricRecipeProvider {
    public ModRecipes(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                var items = registryLookup.lookupOrThrow(Registries.ITEM);
                ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, ModBlocks.FLUID_HOPPER_BLOCK)
                        .pattern("c c")
                        .pattern("cBc")
                        .pattern(" c ")
                        .define('B', Items.BUCKET)
                        .define('c', Items.COPPER_INGOT)
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .unlockedBy("has_bucket", has(Items.BUCKET))
                        .save(output);
                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.REDSTONE, ModBlocks.FLUID_OMNIHOPPER_BLOCK)
                        .requires(ModBlocks.FLUID_HOPPER_BLOCK.asItem())
                        .requires(Items.COPPER_INGOT)
                        .unlockedBy("has_fluid_hopper", has(ModBlocks.FLUID_HOPPER_BLOCK.asItem()))
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(output);
                ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, ModBlocks.OPEN_BOX_BLOCK)
                        .pattern("w w")
                        .pattern("w w")
                        .pattern("www")
                        .define('w', ItemTags.PLANKS)
                        .unlockedBy("has_planks", has(ItemTags.PLANKS))
                        .save(output);
                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.REDSTONE, ModBlocks.OMNIHOPPER_BLOCK)
                        .requires(Items.HOPPER)
                        .requires(Items.COPPER_INGOT)
                        .unlockedBy("has_hopper", has(Items.HOPPER))
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(output);
                ShapedRecipeBuilder.shaped(items, RecipeCategory.REDSTONE, ModBlocks.WOODEN_HOPPER_BLOCK)
                        .pattern("w w")
                        .pattern("wCw")
                        .pattern(" w ")
                        .define('C', Items.CHEST)
                        .define('w', ItemTags.PLANKS)
                        .unlockedBy("has_chest", has(Items.CHEST))
                        .unlockedBy("has_planks", has(ItemTags.PLANKS))
                        .save(output);
                ShapelessRecipeBuilder.shapeless(items, RecipeCategory.REDSTONE, ModBlocks.WOODEN_OMNIHOPPER_BLOCK)
                        .requires(ModBlocks.WOODEN_HOPPER_BLOCK.asItem())
                        .requires(Items.COPPER_INGOT)
                        .unlockedBy("has_wooden_hopper", has(ModBlocks.WOODEN_HOPPER_BLOCK.asItem()))
                        .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "ModRecipes";
    }
}
