package dev.xkmc.gensokyolegacy.content.entity.characters.maiden;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarisaRenderer extends GeoEntityRenderer<MarisaEntity> {
	public MarisaRenderer(EntityRendererProvider.Context context) {
		super(context, new MarisaModel());
	}
}
