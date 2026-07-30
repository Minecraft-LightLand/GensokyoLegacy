package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public record QuestHandle(Holder<Quest> quest, DialogOption<?> dialog) implements IDialogHandle {

	@Override
	public Component display() {
		return dialog.display();
	}

}
