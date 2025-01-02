package dev.xkmc.gensokyolegacy.content.mechanics.role;

import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public enum RoleCategory {
	HUMAN, YOUKAI,
	;

	public void advanceIfStarted(Player player, double max, int val) {
		var list = GLMeta.ABILITY.type().getOrCreate(player).getAll(player);
		for (var e : list) {
			if (e.role().getCategory() == this) {
				e.data().advance(max, val);
			}
		}
	}

	public Component getName() {
		return Component.literal("");//TODO
	}

}
