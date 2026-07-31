package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.l2itemselector.overlay.TextBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.List;

public class DialogScreen<T extends DialogMenu> extends AbstractContainerScreen<T> {

	private int sel = -1;

	public DialogScreen(T menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

	@Override
	public void renderTransparentBackground(GuiGraphics g) {

	}

	@Override
	protected void renderLabels(GuiGraphics g, int mx, int my) {
	}

	protected boolean click(int btn) {
		if (menu.clickMenuButton(menu.player, btn) && Minecraft.getInstance().gameMode != null) {
			Minecraft.getInstance().gameMode.handleInventoryButtonClick(menu.containerId, btn);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean mouseClicked(double mx, double my, int btn) {
		if (sel >= 0) {
			return click(sel);
		}
		return super.mouseClicked(mx, my, btn);
	}

	@Override
	protected void renderBg(GuiGraphics g, float pt, int mx, int my) {
		int sh = g.guiHeight();
		int sw = g.guiWidth();
		var body = menu.getBodyText();
		if (body.isPresent()) {
			var box = new DialogTextBox(g, 1, 2, sw / 2, sh - 10, (int) (sw * 0.7f), (int) (sh * 0.25f));
			box.renderLongText(font, List.of(body.get()));
		}
		var options = menu.getOptions();
		int n = options.size();
		int x0 = (int) (sw * 0.65f);
		int mw = sw - x0 - 10;
		int h = font.lineHeight;
		int sp = h + 2;
		int totalH = 0;
		sel = -1;
		List<OptionTextBox> list = new ArrayList<>();
		for (var e : options) {
			var box = new OptionTextBox(mw);
			box.set(font, e);
			list.add(box);
			if (totalH > 0) totalH += sp;
			totalH += box.h;
		}
		int y = (sh - totalH) / 2;
		for (int i = 0; i < n; i++) {
			var e = list.get(i);
			var rect = e.draw(g, font, x0, y);
			y += e.h + sp;
			if (rect.contains(mx, my)) {
				sel = i;
			}
		}
	}

	public static class DialogTextBox extends TextBox {

		public final int minH;

		public DialogTextBox(GuiGraphics g, int anchorX, int anchorY, int x, int y, int width, int minH) {
			super(g, anchorX, anchorY, x, y, width);
			this.minH = minH;
		}

		public void renderTooltipInternal(Font font, List<ClientTooltipComponent> list) {
			if (list.isEmpty()) {
				return;
			}
			int w = 0;
			int h = list.size() == 1 ? -2 : 0;

			for (ClientTooltipComponent c : list) {
				int wi = c.getWidth(font);
				if (wi > w) {
					w = wi;
				}

				h += c.getHeight();
			}

			int fh = Math.max(minH, h);
			int fw = Math.max(maxW, w);

			Vector2ic pos = this.positionTooltip(this.g.guiWidth(), this.g.guiHeight(), this.x0, this.y0, fw, fh);
			int xf = pos.x();
			int yf = pos.y();
			this.g.pose().pushPose();
			int z = 400;
			this.g.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(this.g, xf, yf, fw, fh, z, this.bg, this.bg, this.bs, this.be));
			this.g.pose().translate(0.0F, 0.0F, (float) z);
			int yi = yf;

			for (int i = 0; i < list.size(); ++i) {
				ClientTooltipComponent c = list.get(i);
				c.renderText(font, xf, yi, this.g.pose().last().pose(), this.g.bufferSource());
				yi += c.getHeight() + (i == 0 ? 2 : 0);
			}

			yi = yf;

			for (int i = 0; i < list.size(); ++i) {
				ClientTooltipComponent c = list.get(i);
				c.renderImage(font, xf, yi, this.g);
				yi += c.getHeight() + (i == 0 ? 2 : 0);
			}

			this.g.pose().popPose();
		}

	}

	public static class OptionTextBox {

		public int maxW;

		public int w, h;
		List<ClientTooltipComponent> list;

		public OptionTextBox(int width) {
			maxW = width;
		}

		public void set(Font font, Component in) {
			list = font.split(in, this.maxW).stream().map(ClientTooltipComponent::create).toList();
			w = 0;
			h = list.size() == 1 ? -2 : 0;
			for (ClientTooltipComponent c : list) {
				int wi = c.getWidth(font);
				if (wi > w) {
					w = wi;
				}
				h += c.getHeight();
			}
		}

		public Rect2i draw(GuiGraphics g, Font font, int x0, int y0) {
			var box = new TextBox(g, 0, 0, x0, y0, maxW);
			Vector2ic pos = box.positionTooltip(g.guiWidth(), g.guiHeight(), x0, y0, w, h);
			int xf = pos.x();
			int yf = pos.y();
			g.pose().pushPose();
			int z = 400;
			g.drawManaged(() -> TooltipRenderUtil.renderTooltipBackground(g, xf, yf, w, h, z, box.bg, box.bg, box.bs, box.be));
			g.pose().translate(0.0F, 0.0F, (float) z);
			int yi = yf;

			for (int i = 0; i < list.size(); ++i) {
				ClientTooltipComponent c = list.get(i);
				c.renderText(font, xf, yi, g.pose().last().pose(), g.bufferSource());
				yi += c.getHeight() + (i == 0 ? 2 : 0);
			}

			yi = yf;

			for (int i = 0; i < list.size(); ++i) {
				ClientTooltipComponent c = list.get(i);
				c.renderImage(font, xf, yi, g);
				yi += c.getHeight() + (i == 0 ? 2 : 0);
			}

			g.pose().popPose();
			return new Rect2i(xf, yf, w, h);
		}

	}

}
