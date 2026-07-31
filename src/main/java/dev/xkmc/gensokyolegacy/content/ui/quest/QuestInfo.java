package dev.xkmc.gensokyolegacy.content.ui.quest;

import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestData;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class QuestInfo {

	private final Quest quest;
	private final QuestData data;

	public QuestInfo(Quest quest, QuestData data) {
		this.quest = quest;
		this.data = data;
	}

	public ArrayList<Component> getReqText(Player player) {
		ArrayList<Component> ans = new ArrayList<>();
		ans.add(Component.translatable(quest.description()));
		for (var e : quest.requirements().entrySet()) {
			ans.addAll(e.getValue().getDesc(player, data.progress.getOrDefault(e.getKey(), 0)));
		}
		return ans;
	}

	public List<Component> getSideBarText(Player player) {
		List<Component> ans = getReqText(player);
		ans.addFirst(Component.translatable(quest.title()).withStyle(ChatFormatting.UNDERLINE));
		return ans;
	}

	public List<Component> getInfoPageText(Player player) {
		List<Component> ans = getReqText(player);
		ans.addFirst(GLLang.QUEST$CHARACTER.get(quest.character().getDescription()).withStyle(ChatFormatting.UNDERLINE));
		return ans;
	}

}
