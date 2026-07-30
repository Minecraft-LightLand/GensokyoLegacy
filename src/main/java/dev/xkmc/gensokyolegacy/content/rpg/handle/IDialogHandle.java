package dev.xkmc.gensokyolegacy.content.rpg.handle;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface IDialogHandle {

	Component display();

	void openMenu(ServerPlayer sp, YoukaiEntity character);

}
