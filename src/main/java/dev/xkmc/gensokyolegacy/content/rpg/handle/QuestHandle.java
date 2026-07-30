package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import net.minecraft.core.Holder;

public record QuestHandle(Holder<Quest> quest, Holder<Dialog> dialog) implements IDialogHandle {

}
