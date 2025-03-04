package dev.xkmc.gensokyolegacy.content.client.tab;

import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoleTabScreen extends BaseTextScreen {

	protected RoleTabScreen(Component title) {
		super(title, L2Tabs.loc("textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, GLMisc.TAB_ROLE.get());
	}

	@Override
	public void render(GuiGraphics g, int mx, int my, float pTick) {
		super.render(g, mx, my, pTick);
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		int x = leftPos + 8;
		int y = topPos + 6;
		for (var comp : getLines(player)) {
			g.drawString(font, comp, x, y, 0, false);
			if (mx > x && mx < x + font.width(comp) && my > y && my < y + 10) {

			}
			y += 10;
		}
	}

	private List<Component> getLines(Player player) {
		List<Component> list = new ArrayList<>();
		var data = GLMeta.ABILITY.type().getOrCreate(player).getAll(player);
		data.sort(Comparator.comparingInt(e -> -e.data().getProgress()));
		if (data.isEmpty()) {
			list.add(GLLang.TAB$NO_ROLE.get());
		} else {
			var first = data.getFirst();
			list.add(GLLang.TAB$MAIN_ROLE.get(first.role().getName(), first.role().getCategory().getName()));
			for (var e : data) {
				list.add(GLLang.TAB$ROLE_PROGRESS.get(e.role().getName(), e.data().getProgress() / 10 + "%"));
			}
		}
		return list;
	}

}
