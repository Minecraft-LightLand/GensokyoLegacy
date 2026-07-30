package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;

public interface QuestReward<T extends Record & QuestReward<T>> extends CodecElement<T> {

}
