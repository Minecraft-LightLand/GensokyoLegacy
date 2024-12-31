package dev.xkmc.gensokyolegacy.content.role.core;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public enum RoleCategory {
	HUMAN, YOUKAI,
	;

	public void advanceIfStarted(Player player, double max, int val) {
		//TODO
	}

	public Component getName() {
		return Component.literal("");//TODO
	}
}
