package nl.enjarai.omnihopper.util;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.WeatheringCopper;

public interface TextureMapProvider {
    TextureMapping getTextureMap();

    static TextureMapping forHopperType(Identifier id) {
        return new TextureMapping()
                .put(TextureSlot.PARTICLE, getSubId(id, "_side"))
                .put(TextureSlot.SIDE, getSubId(id, "_side"))
                .put(TextureSlot.TOP, getSubId(id, "_top"))
                .put(TextureSlot.BOTTOM, getSubId(id, "_bottom"))
                .put(TextureSlot.INSIDE, getSubId(id, "_inside"));
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
                .put(TextureSlot.PARTICLE, Identifier.withDefaultNamespace("block/hopper_outside"))
                .put(TextureSlot.SIDE, Identifier.withDefaultNamespace("block/hopper_outside"))
                .put(TextureSlot.TOP, Identifier.withDefaultNamespace("block/hopper_top"))
                .put(TextureSlot.BOTTOM, Identifier.withDefaultNamespace("block/hopper_outside"))
                .put(TextureSlot.INSIDE, Identifier.withDefaultNamespace("block/hopper_inside"));
    }

    static Identifier getSubId(Identifier id, String suffix) {
        return id.withPath(path -> "block/" + path + suffix);
    }
}
