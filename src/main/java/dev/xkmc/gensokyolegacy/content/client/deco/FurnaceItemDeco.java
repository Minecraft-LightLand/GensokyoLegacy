package dev.xkmc.gensokyolegacy.content.client.deco;

import dev.xkmc.gensokyolegacy.content.item.tool.MiniFurnace1;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public class FurnaceItemDeco implements IItemDecorator {

	@Override
	public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
		var player = Minecraft.getInstance().player;
		if (player == null) return false;
		var data = stack.getOrDefault(GLItems.DC_FURNACE_1, MiniFurnace1.Data.DEF);
		if (data.state() == MiniFurnace1.State.OFF) return false;
		var inv = player.getInventory();
		for (int i = 9; i < 36; i++) {
			if (inv.getItem(i) == stack) {
				int r = (i - 9) / 9;
				int c = i % 9;
				g.pose().pushPose();
				g.pose().translate(0, 0, 2000);
				renderImpl(g, data, 0, x - 18, y, r, c - 1);
				renderImpl(g, data, 1, x, y - 18, r - 1, c);
				renderImpl(g, data, 2, x + 18, y, r, c + 1);
				renderImpl(g, data, 3, x, y + 18, r + 1, c);
				g.pose().popPose();
				return true;
			}
		}
		return false;
	}

	private void renderImpl(GuiGraphics g, MiniFurnace1.Data data, int i, int x, int y, int r0, int c0) {
		if (r0 < 0 || r0 >= 3 || c0 < 0 || c0 >= 9) return;
		var entry = data.data()[i];
		if (entry == null) return;
		float f = 1f * entry.time() / entry.max();
		if (f > 0.0F) {
			int y0 = y + Mth.floor(16.0F * (1 - f));
			int y1 = y0 + Mth.ceil(16.0F * f);
			g.fill(RenderType.guiOverlay(), x, y0, x + 16, y1, Integer.MAX_VALUE);
		}
	}

}
