package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class BlackBallLayer<T extends RumiaEntity> extends RenderLayer<T, RumiaModel<T>> {

	public static final ResourceLocation TEX = GensokyoLegacy.loc("textures/entity/black_ball.png");

	private final BlackBallModel<T> model;

	public BlackBallLayer(RenderLayerParent<T, RumiaModel<T>> pRenderer, EntityModelSet pModelSet) {
		super(pRenderer);
		this.model = new BlackBallModel<>(pModelSet.bakeLayer(BlackBallModel.LAYER_LOCATION));
	}

	public void render(PoseStack pose, MultiBufferSource buffer, int light, T e, float swing, float swingAmp, float pTick, float age, float yaw, float pitch) {
		if (!e.isCharged()) return;
		Minecraft minecraft = Minecraft.getInstance();
		boolean flag = minecraft.shouldEntityAppearGlowing(e) && e.isInvisible();
		if (!e.isInvisible() || flag) {
			VertexConsumer vertexconsumer;
			if (flag) {
				vertexconsumer = buffer.getBuffer(RenderType.outline(this.getTextureLocation(e)));
			} else {
				vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(e)));
			}
			this.model.prepareMobModel(e, swing, swingAmp, pTick);
			this.model.setupAnim(e, swing, swingAmp, age, yaw, pitch);
			this.model.renderToBuffer(pose, vertexconsumer, light, LivingEntityRenderer.getOverlayCoords(e, 0.0F));
		}
	}

	@Override
	protected ResourceLocation getTextureLocation(T pEntity) {
		return TEX;
	}
}