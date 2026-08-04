package dev.xkmc.gensokyolegacy.content.ui.trade;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TradeScreen extends AbstractContainerScreen<TradeMenu> {

	public TradeScreen(TradeMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {

	}

}
