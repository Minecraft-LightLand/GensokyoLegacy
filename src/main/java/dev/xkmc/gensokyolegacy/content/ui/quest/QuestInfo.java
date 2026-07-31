package dev.xkmc.gensokyolegacy.content.ui.quest;

import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestData;
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

	public Component getTitle() {
		return Component.translatable(quest.title());
	}

	public List<Component> getBody(Player player) {
		List<Component> ans = new ArrayList<>();
		ans.add(Component.translatable(quest.description()));
		for (var e : quest.requirements().entrySet()) {
			ans.addAll(e.getValue().getDesc(player, data.progress.getOrDefault(e.getKey(), 0)));
		}
		return ans;
	}

}
