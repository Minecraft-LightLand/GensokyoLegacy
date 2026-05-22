package dev.xkmc.gensokyolegacy.content.entity.youkai.model;

import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MarisaEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class MarisaModel extends GeoModel<MarisaEntity> {

	private final Identifier model = GensokyoLegacy.loc("geo/marisa.geo.json");
	private final Identifier texture = GensokyoLegacy.loc("textures/geo/marisa.png");
	private final Identifier animations = GensokyoLegacy.loc("animations/marisa.animation.json");

	@Override
	public Identifier getModelResource(MarisaEntity animatable) {
		return model;
	}

	@Override
	public Identifier getTextureResource(MarisaEntity animatable) {
		return texture;
	}

	@Override
	public Identifier getAnimationResource(MarisaEntity animatable) {
		return animations;
	}
}
