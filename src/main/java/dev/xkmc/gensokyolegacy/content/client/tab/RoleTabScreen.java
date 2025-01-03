package dev.xkmc.gensokyolegacy.content.client.tab;

import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class RoleTabScreen extends BaseTextScreen {

	protected RoleTabScreen(Component title) {
		super(title, L2Tabs.loc("textures/gui/empty.png"));
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		int x = leftPos + 8;
		int y = topPos + 6;
		List<Component> list = new ArrayList<>();
		
		for (var comp : list) {
			g.drawString(font, comp, x, y, 0, false);
			if (mx > x && mx < x + font.width(comp) && my > y && my < y + 10) {

			}
			y += 10;
		}
	}
}
