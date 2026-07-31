package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.QuestTrigger;
import net.minecraft.server.level.ServerPlayer;

public interface QuestRequirement<
		E extends Record & QuestRequirement<E, T>,
		T extends Record & QuestTrigger<T>
		> extends CodecElement<E> {

	default int getMaxProgress() {
		return 0;
	}

	default boolean canComplete(ServerPlayer sp) {
		return true;
	}

	default void doComplete(ServerPlayer sp) {

	}

	Class<T> getTrigger();

	default boolean match(T trigger) {
		return true;
	}

}
