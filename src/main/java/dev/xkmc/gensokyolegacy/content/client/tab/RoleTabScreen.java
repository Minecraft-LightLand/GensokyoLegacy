package dev.xkmc.gensokyolegacy.content.client.tab;

import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
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
	public void render(GuiGraphics g, int mx, int my, float pTick) {
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
			list.add(GLLang.TAB_NO_ROLE.get());
		} else {
			var first = data.getFirst();
			list.add(GLLang.TAB_MAIN_ROLE.get(first.role().getName(), first.role().getCategory().getName()));
			for (var e : data) {
				list.add(GLLang.TAB_ROLE_PROGRESS.get(e.role().getName(), e.data().getProgress() / 10 + "%"));
			}
		}
		return list;
	}

}
