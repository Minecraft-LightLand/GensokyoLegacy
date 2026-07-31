package dev.xkmc.gensokyolegacy.content.ui.quest;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class QuestTab extends TabBase<InvTabData, QuestTab> {

	public QuestTab(int index, TabToken<InvTabData, QuestTab> token, TabManager<InvTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	public void onTabClicked() {
		Minecraft.getInstance().setScreen(new QuestInfoScreen());
	}

}
