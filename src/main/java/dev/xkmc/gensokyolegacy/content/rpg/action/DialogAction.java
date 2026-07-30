package dev.xkmc.gensokyolegacy.content.rpg.action;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecElement;

public interface DialogAction<T extends Record & DialogAction<T>> extends CodecElement<T> {

	void execute(ActionContext context);

}
