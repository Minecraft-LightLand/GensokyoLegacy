package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public interface DialogOption<T extends DialogOption<T>> extends CodecElement<T>, GatedEntry {

	Component display();

	Optional<Holder<Dialog>> next();

	List<DialogAction<?>> actions();

}
