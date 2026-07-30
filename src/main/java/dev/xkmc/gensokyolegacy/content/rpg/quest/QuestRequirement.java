package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import net.minecraft.server.level.ServerPlayer;

public interface QuestRequirement<T extends Record & QuestRequirement<T>> extends CodecElement<T> {

	default int getMaxProgress() {
		return 0;
	}

	default boolean canComplete(ServerPlayer sp) {
		return true;
	}

	default void doComplete(ServerPlayer sp) {

	}

}
