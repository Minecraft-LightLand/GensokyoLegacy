package dev.xkmc.gensokyolegacy.content.ui.dialog;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FirstDialogScreen extends DialogScreen<FirstDialogMenu> {

	public FirstDialogScreen(FirstDialogMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

}
