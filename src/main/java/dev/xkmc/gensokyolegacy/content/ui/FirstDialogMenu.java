package dev.xkmc.gensokyolegacy.content.ui;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.ServerCharacterDialogManager;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FirstDialogMenu extends DialogMenu {

	public static FirstDialogMenu create(MenuType<?> menu, int wid, ServerPlayer sp, YoukaiEntity character) {
		var data = ServerCharacterDialogManager.get(sp.serverLevel(), character.getType());
		var handles = data.getInitialConversation(sp, character);
		var options = new ArrayList<Component>();
		for (var e : handles)
			options.add(e.display());
		return new FirstDialogMenu(menu, wid, sp, character, handles, options);
	}

	private final List<Component> options;

	private final @Nullable List<IDialogHandle> handles;

	public FirstDialogMenu(MenuType<?> menu, int wid, ServerPlayer sp, YoukaiEntity character, List<IDialogHandle> handles, List<Component> options) {
		super(menu, wid, sp, character);
		this.handles = handles;
		this.options = options;
	}

	public FirstDialogMenu(MenuType<?> menu, int wid, Player player, @Nullable YoukaiEntity character, List<Component> options) {
		super(menu, wid, player, character);
		this.options = options;
		handles = null;
	}

	@Override
	public List<Component> getOptions() {
		return options;
	}

}
