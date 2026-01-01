package nl.enjarai.omnihopper.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.registry.entry.RegistryEntry;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(targets = "net.minecraft.client.data.ModelProvider$BlockStateSuppliers")
public class ModelProvider$BlockStateSuppliers {
    @ModifyExpressionValue(method = "validate", at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    private static Stream<RegistryEntry.Reference<Block>> patchOpenBlockOutOfValidation(Stream<RegistryEntry.Reference<Block>> original) {
        return original.filter(it -> !ModBlocks.OPEN_BOX_BLOCK.equals(it.value()));
    }
}
