package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.ui.SimpleDialogProvider;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public record QuestHandle(Holder<Quest> quest, DialogOption<?> dialog) implements IDialogHandle {

	@Override
	public Component display() {
		return dialog.display();
	}

	@Override
	public void openMenu(ServerPlayer sp, YoukaiEntity character) {
		var next = dialog.next();
		if (next.isEmpty()) return;
		new SimpleDialogProvider(sp, character, this, next.get()).open();
	}

}
