package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import net.minecraft.network.chat.Component;

public interface DialogOption<T extends DialogOption<T>> extends CodecElement<T>, GatedEntry {

	Component display();

}
