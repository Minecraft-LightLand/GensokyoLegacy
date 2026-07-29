package dev.xkmc.gensokyolegacy.content.quest.core;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.server.level.ServerPlayer;

public interface QuestCondition<T extends Record & QuestCondition<T>> extends QuestElement<T> {

	boolean test(ServerPlayer pl, YoukaiEntity ch);

}
