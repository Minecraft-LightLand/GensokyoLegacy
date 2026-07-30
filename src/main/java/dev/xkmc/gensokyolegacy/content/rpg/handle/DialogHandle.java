package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogStarter;
import net.minecraft.core.Holder;

public record DialogHandle(Holder<DialogStarter> starter) implements IDialogHandle {

}
