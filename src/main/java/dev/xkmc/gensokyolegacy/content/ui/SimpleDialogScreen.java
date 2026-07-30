package dev.xkmc.gensokyolegacy.content.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class SimpleDialogScreen extends DialogScreen<SimpleDialogMenu> {

	public SimpleDialogScreen(SimpleDialogMenu menu, Inventory inv, Component title) {
		super(menu, inv, title);
	}

}
