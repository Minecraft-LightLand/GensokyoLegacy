package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogStarter;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

public record DialogHandle(Holder<DialogStarter> starter) implements IDialogHandle {

	@Override
	public Component display() {
		return Component.translatable(starter.value().text());
	}

}
