package nl.enjarai.omnihopper.util;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopper;

public interface TextureMapProvider {
    TextureMapping getTextureMap();

    static TextureMapping forHopperType(Identifier id) {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, mat(getSubId(id, "_side")))
                .put(TextureSlot.SIDE, mat(getSubId(id, "_side")))
                .put(TextureSlot.TOP, mat(getSubId(id, "_top")))
                .put(TextureSlot.BOTTOM, mat(getSubId(id, "_bottom")))
                .put(TextureSlot.INSIDE, mat(getSubId(id, "_inside")));
    }

    static TextureMapping forOxidizableHopperType(Identifier id, WeatheringCopper.WeatherState degradationLevel) {
        var prefix = switch (degradationLevel) {
            case UNAFFECTED -> "";
            case EXPOSED -> "exposed_";
            case WEATHERED -> "weathered_";
            case OXIDIZED -> "oxidized_";
        };
        return forHopperType(id.withPath(path -> prefix + path));
    }

    static TextureMapping forVanillaHopper() {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, mat(Identifier.withDefaultNamespace("block/hopper_outside")))
                .put(TextureSlot.SIDE, mat(Identifier.withDefaultNamespace("block/hopper_outside")))
                .put(TextureSlot.TOP, mat(Identifier.withDefaultNamespace("block/hopper_top")))
                .put(TextureSlot.BOTTOM, mat(Identifier.withDefaultNamespace("block/hopper_outside")))
                .put(TextureSlot.INSIDE, mat(Identifier.withDefaultNamespace("block/hopper_inside")));
    }

    static Identifier getSubId(Identifier id, String suffix) {
        return id.withPath(path -> "block/" + path + suffix);
    }

    private static Material mat(Identifier id) {
        return new Material(id);
    }
}
