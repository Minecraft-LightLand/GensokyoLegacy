package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record ClientHandle(Optional<Holder<Quest>> quest) implements IDialogHandle {

	@Override
	public Component display() {
		return Component.empty();
	}

	@Override
	public void openMenu(ServerPlayer sp, YoukaiEntity character) {

	}

	@Override
	public Optional<Holder<Quest>> getQuest() {
		return quest;
	}
}
