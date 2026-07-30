package dev.xkmc.gensokyolegacy.content.ui.dialog;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class DialogScreen<T extends DialogMenu> extends AbstractContainerScreen<T> {

	public DialogScreen(T menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
	}

}
