package dev.xkmc.gensokyolegacy.content.rpg.dialog;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public interface DialogOption<T extends DialogOption<T>> extends CodecElement<T>, GatedEntry {

	Component display();

	Optional<Holder<Dialog>> next();

}
