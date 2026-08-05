package dev.xkmc.gensokyolegacy.content.ui.dialog;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SimpleDialogScreen extends DialogScreen<SimpleDialogMenu> {

	public SimpleDialogScreen(SimpleDialogMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
		super.renderBg(g, pt, mx, my);
		renderQuestInfo(g, menu.handle.getQuest());
	}

}
