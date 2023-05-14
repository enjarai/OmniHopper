package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.BlockItem;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.items.ModItems;
import nl.enjarai.omnihopper.util.DatagenBlock;

public class ModModels extends FabricModelProvider {
    public ModModels(FabricDataGenerator output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (var block : ModBlocks.ALL) {
            if (block instanceof DatagenBlock datagenBlock) {
                datagenBlock.generateBlockStateModels(blockStateModelGenerator);
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (var item : ModItems.ALL) {
            if (item instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof DatagenBlock datagenBlock) {
                    datagenBlock.generateItemModel(itemModelGenerator, blockItem);
                }
            }
        }
    }
}
