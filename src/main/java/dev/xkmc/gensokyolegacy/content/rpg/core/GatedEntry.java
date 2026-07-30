package dev.xkmc.gensokyolegacy.content.rpg.core;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface GatedEntry {

	List<QuestCondition<?>> conditions();

	default boolean match(ServerPlayer sp, YoukaiEntity e) {
		for (var c : conditions()) {
			if (!c.test(sp, e))
				return false;
		}
		return true;
	}

}
