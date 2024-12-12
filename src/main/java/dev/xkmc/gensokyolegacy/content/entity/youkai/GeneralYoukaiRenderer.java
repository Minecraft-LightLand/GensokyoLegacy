package dev.xkmc.gensokyolegacy.content.entity.youkai;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TLMClientCompat;
import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaModel;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GeneralYoukaiRenderer<T extends GeneralYoukaiEntity> extends MobRenderer<T, EntityModel<T>> {

	public static final ResourceLocation TEX = GensokyoLegacy.loc("textures/entity/rumia.png");

	public GeneralYoukaiRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new PlaceHolderModel<>(ctx.bakeLayer(RumiaModel.LAYER_LOCATION)), 0.2f);
		addLayer(new SpellCircleLayer<>(this));
	}

	@Override
	public void render(T e, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		if (TLMClientCompat.delegateRender(e, yaw, pTick, pose, buffer, light)) {
			return;
		}
		super.render(e, yaw, pTick, pose, buffer, light);
	}

	@Override
	public ResourceLocation getTextureLocation(T pEntity) {
		return TEX;
	}

}
