package dev.xkmc.gensokyolegacy.content.client.deco;

import dev.xkmc.gensokyolegacy.content.item.tool.MiniFurnace1;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class FurnaceItemDeco {

	public static void renderSlot(GuiGraphics g, ItemStack stack, Inventory inv, int i, int x, int y) {
		if (i < 9 || i >= 36) return;
		int r = (i - 9) / 9;
		int c = i % 9;
		renderImpl(g, inv, 0, x, y, r, c + 1, r, c);
		renderImpl(g, inv, 1, x, y, r + 1, c, r, c);
		renderImpl(g, inv, 2, x, y, r, c - 1, r, c);
		renderImpl(g, inv, 3, x, y, r - 1, c, r, c);
	}

	private static void renderImpl(GuiGraphics g, Inventory inv, int i, int x, int y, int r0, int c0, int r1, int c1) {

		if (r0 < 0 || r0 >= 3 || c0 < 0 || c0 >= 9) return;
		int s0 = r0 * 9 + c0 + 9;
		var stack = inv.getItem(s0);
		if (!stack.is(GLItems.MINI_FURNACE_1)) return;
		var data = stack.getOrDefault(GLItems.DC_FURNACE_1, MiniFurnace1.Data.DEF);
		if (data.state() == MiniFurnace1.State.OFF) return;

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
