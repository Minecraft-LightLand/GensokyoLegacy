package dev.xkmc.gensokyolegacy.content.block.shelf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {

	public ShelfRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(ShelfBlockEntity be, float pt, PoseStack pose, MultiBufferSource source, int light, int overlay) {
		if (be.stack.isEmpty()) return;
		pose.pushPose();
		pose.translate(0.5f, 0.5f, 0.5f);
		pose.mulPose(Axis.YP.rotationDegrees(-be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180));
		pose.pushPose();
		pose.translate(0, -1 / 16f, 6 / 16f);
		pose.scale(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getItemRenderer().renderStatic(be.stack, ItemDisplayContext.FIXED, light, overlay, pose, source, be.getLevel(), 0);
		pose.popPose();
		pose.translate(0, 7 / 16f, 6 / 16f);
		pose.mulPose(Axis.YP.rotationDegrees(180));
		float r = 1 / 64f;
		pose.scale(r, -r, r);
		Component text = be.getTitle();
		var font = Minecraft.getInstance().font;
		var dx = font.width(text) / 2f;
		font.drawInBatch(text, -dx, 0, 0xff000000, false, pose.last().pose(), source, Font.DisplayMode.POLYGON_OFFSET, 0, light);
		pose.popPose();
	}

}
