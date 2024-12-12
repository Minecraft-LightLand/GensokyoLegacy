package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleLayer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RumiaRenderer extends MobRenderer<RumiaEntity, RumiaModel<RumiaEntity>> {

	public static final ResourceLocation TEX = GensokyoLegacy.loc("textures/entity/rumia.png");

	public RumiaRenderer(EntityRendererProvider.Context context) {
		super(context, new RumiaModel<>(context.bakeLayer(RumiaModel.LAYER_LOCATION)), 0.2F);
		addLayer(new BlackBallLayer<>(this, context.getModelSet()));
		addLayer(new SpellCircleLayer<>(this));
	}

	public ResourceLocation getTextureLocation(RumiaEntity entity) {
		return TEX;
	}

	@Override
	protected void setupRotations(RumiaEntity rumia, PoseStack pose, float bob, float yBodyRot, float partialTick, float scale) {
		if (rumia.isBlocked()) {
			pose.translate(0, 0.2, 0);
			pose.mulPose(Axis.XP.rotationDegrees(90));
			pose.translate(0, -0.85, 0);
		} else super.setupRotations(rumia, pose, bob, yBodyRot, partialTick, scale);
	}

	@Override
	public void render(RumiaEntity rumia, float yaw, float pTick, PoseStack pose, MultiBufferSource buffer, int light) {
		super.render(rumia, yaw, pTick, pose, buffer, light);
	}

}
