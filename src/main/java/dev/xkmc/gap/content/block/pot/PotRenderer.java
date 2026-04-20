package dev.xkmc.gap.content.block.pot;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.gensokyolegacy.util.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public class PotRenderer implements BlockEntityRenderer<PotBlockEntity> {

	private final ItemRenderer itemRenderer;

	public PotRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(PotBlockEntity be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		float h = 0;
		FluidStack fluid = be.fluids.getFluidInTank(0);
		if (!fluid.isEmpty()) {
			h = 12f / 16 * fluid.getAmount() / 1000;
			FluidRenderer.renderFluidBox(fluid, 3f / 16, 2f / 16, 3f / 16,
					13f / 16, 2f / 16 + h, 13f / 16,
					buffer, pose, light, false, 0);
		}
		List<ItemStack> list = be.items.getAsList();
		int i = (int) be.getBlockPos().asLong();

		for (int j = 0; j < list.size(); ++j) {
			ItemStack stack = list.get(j);
			if (!stack.isEmpty()) {
				pose.pushPose();
				pose.translate(0.5F, h + 3f / 16, 0.5F);
				pose.mulPose(Axis.YP.rotationDegrees(j * 45));
				pose.mulPose(Axis.XP.rotationDegrees(70.0F));
				float dist = 2f / 16;
				pose.translate(-dist, -dist, 0.0F);
				pose.scale(0.375F, 0.375F, 0.375F);
				this.itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, light, overlay, pose, buffer, be.getLevel(), i + j);
				pose.popPose();
			}
		}
	}

}
