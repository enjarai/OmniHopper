package nl.enjarai.omnihopper.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.math.Direction;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.BasicHopperBlock;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.OmniHopperBlock;
import nl.enjarai.omnihopper.util.TextureMapProvider;
import nl.enjarai.omnihopper.items.ModItems;

import java.util.Optional;

public class ModModels extends FabricModelProvider {
    public ModModels(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (var block : ModBlocks.ALL) {
            if (block instanceof TextureMapProvider textureProvider) {
                for (var direction : Direction.values()) {
                    var suffix = "_" + direction.getName();

                    blockStateModelGenerator.createSubModel(
                            block, suffix,
                            new Model(
                                    Optional.of(OmniHopper.id("block/hopper" + suffix)),
                                    Optional.empty(),
                                    TextureKey.PARTICLE, TextureKey.SIDE,
                                    TextureKey.TOP, TextureKey.INSIDE, TextureKey.BOTTOM
                            ),
                            id -> textureProvider.getTextureMap()
                    );
                }

                if (block instanceof BasicHopperBlock) {
                    var variants = BlockStateVariantMap.create(BasicHopperBlock.POINTY_BIT);

                    variants.register(
                            direction -> BlockStateVariant.create().put(
                                    VariantSettings.MODEL,
                                    ModelIds.getBlockSubModelId(block, "_" + direction.getName())
                            )
                    );

                    blockStateModelGenerator.blockStateCollector.accept(
                            VariantsBlockStateSupplier.create(block).coordinate(variants));
                } else if (block instanceof OmniHopperBlock) {
                    var variants = BlockStateVariantMap.create(OmniHopperBlock.POINTY_BIT, OmniHopperBlock.SUCKY_BIT);

                    variants.register(
                            (pointy, sucky) -> {
                                var settings = HopperRotation.getFor(sucky, pointy);
                                return BlockStateVariant.create().put(
                                                VariantSettings.MODEL,
                                                ModelIds.getBlockSubModelId(block, "_" + settings.modelDirection().getName())
                                        )
                                        .put(VariantSettings.X, settings.rotX())
                                        .put(VariantSettings.Y, settings.rotY());
                            }
                    );

                    blockStateModelGenerator.blockStateCollector.accept(
                            VariantsBlockStateSupplier.create(block).coordinate(variants));
                }
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        ModItems.ALL.forEach(item -> itemModelGenerator.register(item, Models.GENERATED));
    }
}
