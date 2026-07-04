package dev.xkmc.gensokyolegacy.content.block.shelf;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {

	public ShelfRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(ShelfBlockEntity be, float pt, PoseStack pose, MultiBufferSource source, int light, int overlay) {
		if (be.stack.isEmpty()) return;
		pose.pushPose();
		pose.translate(0.5f, 0.5f, 0.5f);
		Minecraft.getInstance().getItemRenderer().renderStatic(be.stack, ItemDisplayContext.GROUND, light, overlay, pose, source, be.getLevel(), 0);
		pose.popPose();
	}

}
