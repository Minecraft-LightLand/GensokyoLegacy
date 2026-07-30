package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;

public interface QuestRequirement<T extends Record & QuestRequirement<T>> extends CodecElement<T> {

	int getMaxProgress();

}
