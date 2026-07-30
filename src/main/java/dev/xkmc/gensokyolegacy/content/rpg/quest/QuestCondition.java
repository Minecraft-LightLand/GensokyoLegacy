package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import net.minecraft.server.level.ServerPlayer;

public interface QuestCondition<T extends Record & QuestCondition<T>> extends CodecElement<T> {

	boolean test(ServerPlayer pl, YoukaiEntity ch);

}
