//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package dev.xkmc.gensokyolegacy.content.block.portal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GapPortalRenderer implements BlockEntityRenderer<GapPortalBlockEntity> {
	public static final ResourceLocation END_SKY_LOCATION = ResourceLocation.withDefaultNamespace("textures/environment/end_sky.png");
	public static final ResourceLocation END_PORTAL_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/end_portal.png");
	public static final ResourceLocation FRAME = GensokyoLegacy.loc("textures/block/utensil/gap_portal.png");
	public static final ResourceLocation FRAME_BACK = GensokyoLegacy.loc("textures/block/utensil/gap_portal_back.png");

	public GapPortalRenderer(BlockEntityRendererProvider.Context ctx) {
	}

	public void render(GapPortalBlockEntity e, float pTick, PoseStack pose, MultiBufferSource source, int light, int overlay) {
		if (e.getBlockState().getValue(BlockStateProperties.HALF) == Half.TOP) return;
		var cam = Minecraft.getInstance().gameRenderer.getMainCamera();
		pose.pushPose();
		pose.translate(0.5f, 0, 0.5f);
		pose.mulPose(Axis.YP.rotationDegrees(180 - cam.getYRot()));
		var mat = pose.last().pose();
		var normal = pose.last();
		{
			var vc = source.getBuffer(RenderType.entityCutout(FRAME));
			vc.addVertex(mat, -0.5f, 0, 0.01f).setColor(-1).setUv(0, 0).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, 1);
			vc.addVertex(mat, 0.5f, 0, 0.01f).setColor(-1).setUv(1, 0).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, 1);
			vc.addVertex(mat, 0.5f, 2, 0.01f).setColor(-1).setUv(1, 1).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, 1);
			vc.addVertex(mat, -0.5f, 2, 0.01f).setColor(-1).setUv(0, 1).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, 1);
		}
		{
			var vc = source.getBuffer(RenderType.entityCutout(FRAME_BACK));
			vc.addVertex(mat, -0.5f, 2, -0.01f).setColor(-1).setUv(0, 1).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, -1);
			vc.addVertex(mat, 0.5f, 2, -0.01f).setColor(-1).setUv(1, 1).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, -1);
			vc.addVertex(mat, 0.5f, 0, -0.01f).setColor(-1).setUv(1, 0).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, -1);
			vc.addVertex(mat, -0.5f, 0, -0.01f).setColor(-1).setUv(0, 0).setLight(light).setOverlay(overlay).setNormal(normal, 0, 0, -1);
		}
		{
			var vc = source.getBuffer(RenderType.endPortal());
			vc.addVertex(mat, -6 / 16f, 9 / 16f, 0);
			vc.addVertex(mat, 0, 0, 0);
			vc.addVertex(mat, 0, 2, 0);
			vc.addVertex(mat, -6 / 16f, 23 / 16f, 0);

			vc.addVertex(mat, 6 / 16f, 23 / 16f, 0);
			vc.addVertex(mat, 0, 2, 0);
			vc.addVertex(mat, 0, 0, 0);
			vc.addVertex(mat, 6 / 16f, 9 / 16f, 0);
		}
		pose.popPose();
	}

}
