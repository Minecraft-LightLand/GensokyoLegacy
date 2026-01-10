package dev.xkmc.gensokyolegacy.content.client.structure;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class StructureCustomizeScreen extends Screen {

	protected StructureCustomizeScreen() {
		super(Component.empty());
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		super.render(guiGraphics, mouseX, mouseY, partialTick);
	}

	@Override
	protected void init() {
		super.init();
		addRenderableWidget(new Button.Builder(Component.literal("scan"), btn -> StructureRepairManager.onScan())
				.pos(10, 10).size(70, 20).build());
		addRenderableWidget(new Button.Builder(Component.literal("delete"), btn -> StructureRepairManager.onDelete())
				.pos(10, 40).size(70, 20).build());
	}

	public boolean keyPressed(int a, int b, int c) {
		InputConstants.Key mouseKey = InputConstants.getKey(a, b);
		if (Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey)) {
			this.onClose();
			return true;
		} else {
			return super.keyPressed(a, b, c);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
