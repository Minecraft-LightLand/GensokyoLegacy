package dev.xkmc.gensokyolegacy.content.ui.trade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;

public class TradeScreen extends AbstractContainerScreen<TradeMenu> {

	public TradeScreen(TradeMenu cont, Inventory plInv, Component title) {
		super(cont, plInv, title);
	}

	@Override
	protected void init() {
		super.init();
		int y = topPos + 10 - 16 - 2;
		int nextX = leftPos + 143 + 16 - 16;
		int prevX = nextX - 16 - 4;
		addRenderableWidget(new Button.Builder(Component.literal("<"), b -> click(-1))
				.pos(prevX, y).size(16, 16).build());
		addRenderableWidget(new Button.Builder(Component.literal(">"), b -> click(-2))
				.pos(nextX, y).size(16, 16).build());
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {

	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
		var text = Component.literal((menu.getPage() + 1) + "/" + menu.getMaxPage());
		int x = 123 - font.width(text) - 4;
		int y = 10 - 16 - 2 + (16 - font.lineHeight) / 2;
		g.drawString(font, text, x, y, 0x404040, false);
	}

	@Override
	protected void slotClicked(Slot slot, int slotId, int mouseButton, ClickType clickType) {
		if (slot instanceof TradeSlot ts) {
			click(ts.getContainerSlot());
			return;
		}
		super.slotClicked(slot, slotId, mouseButton, clickType);
	}

	protected boolean click(int btn) {
		if (menu.clickMenuButton(menu.player, btn) && Minecraft.getInstance().gameMode != null) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, btn);
			return true;
		} else {
			return false;
		}
	}

}
