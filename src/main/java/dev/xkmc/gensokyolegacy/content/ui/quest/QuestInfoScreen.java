package dev.xkmc.gensokyolegacy.content.ui.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2tabs.tabs.contents.BaseTextScreen;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class QuestInfoScreen extends BaseTextScreen {

	public QuestInfoScreen() {
		super(GLLang.QUEST$TAB.get(), ResourceLocation.fromNamespaceAndPath("l2tabs", "textures/gui/empty.png"));
	}

	@Override
	public void init() {
		super.init();
		new TabManager<>(this, new InvTabData()).init(this::addRenderableWidget, GLMisc.QUEST_TAB.get());
	}

	@Override
	public void renderBackground(GuiGraphics g, int mx, int my, float pt) {
		super.renderBackground(g, mx, my, pt);
		int x = this.leftPos + 8;
		int y = this.topPos + 6;

		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var quest = GLMeta.QUEST.type().getOrCreate(player);
		QuestInfo focus = null;
		g.drawString(this.font, GLLang.QUEST$TAB.get().append(":").withStyle(ChatFormatting.UNDERLINE), x, y, 0, false);
		y += font.lineHeight;
		x += 8;
		for (var e : quest.data.entrySet()) {
			if (!e.getValue().started) continue;
			var holder = player.level().registryAccess().holder(ResourceKey.create(CodecRegistry.Keys.QUEST, e.getKey()));
			if (holder.isEmpty()) continue;
			var comp = Component.translatable(holder.get().value().title());
			if (e.getValue().isCompletable(player, holder.get().value()))
				comp.withStyle(ChatFormatting.DARK_GREEN);
			g.drawString(this.font, comp, x, y, 0, false);
			y += font.lineHeight;
			if (mx > x && mx < x + this.font.width(comp) && my > y && my < y + 10) {
				focus = new QuestInfo(holder.get().value(), e.getValue());
			}
		}
		if (focus != null) {
			g.renderComponentTooltip(this.font, focus.getInfoPageText(player), mx, my);
		}
	}
}
