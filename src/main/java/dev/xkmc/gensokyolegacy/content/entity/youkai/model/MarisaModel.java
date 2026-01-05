package dev.xkmc.gensokyolegacy.content.entity.youkai.model;

import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MarisaEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MarisaModel extends GeoModel<MarisaEntity> {

    private final ResourceLocation model = GensokyoLegacy.loc("geo/marisa.geo.json");
    private final ResourceLocation texture = GensokyoLegacy.loc("textures/geo/marisa.png");
    private final ResourceLocation animations = GensokyoLegacy.loc("animations/marisa.animation.json");

    @Override
    public ResourceLocation getModelResource(MarisaEntity animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(MarisaEntity animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(MarisaEntity animatable) {
        return animations;
    }
}
