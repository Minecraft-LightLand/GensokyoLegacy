package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.gensokyolegacy.content.ui.quest.QuestInfo;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2itemselector.overlay.TextBox;
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
		var quest = menu.handle.getQuest();
		if (quest.isEmpty()) return;
		var data = GLMeta.QUEST.type().getOrCreate(menu.player).getData(quest.get().unwrapKey().orElseThrow().location());
		if (!data.started) return;
		var info = new QuestInfo(quest.get().value(), data);
		new TextBox(g, 0, 1, 10, g.guiHeight() / 2, (int) (g.guiWidth() * 0.4f - 20))
				.renderLongText(font, info.getBody(menu.player));
	}

}
