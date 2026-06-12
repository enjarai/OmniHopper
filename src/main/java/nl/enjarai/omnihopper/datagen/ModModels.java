package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.world.item.BlockItem;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import nl.enjarai.omnihopper.util.DatagenBlock;

public class ModModels extends FabricModelProvider {
    public ModModels(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for (var block : ModBlocks.ALL) {
            if (block instanceof DatagenBlock datagenBlock) {
                datagenBlock.generateBlockStateModels(blockStateModelGenerator);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (var item : ModItems.ALL) {
            if (item instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof DatagenBlock datagenBlock) {
                    datagenBlock.generateItemModel(itemModelGenerator, blockItem);
                }
            }
        }
    }
}
