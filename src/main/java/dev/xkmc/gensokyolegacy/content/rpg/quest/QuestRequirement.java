package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.QuestTrigger;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface QuestRequirement<
		E extends Record & QuestRequirement<E, T>,
		T extends Record & QuestTrigger<T>
		> extends CodecElement<E> {

	default int getMaxProgress() {
		return 0;
	}

	default boolean canComplete(Player sp) {
		return true;
	}

	default void doComplete(ServerPlayer sp) {

	}

	Class<T> getTrigger();

	default int match(T trigger) {
		return 0;
	}

	default int rawMatch(QuestTrigger<?> trigger) {
		if (getTrigger().isInstance(trigger)) {
			return match(Wrappers.cast(trigger));
		}
		return 0;
	}

	List<Component> getDesc(Player player, int progress);

}
