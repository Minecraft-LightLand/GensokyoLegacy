package dev.xkmc.gensokyolegacy.content.mechanics.role;

import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class Role extends NamedEntry<Role> {

	public Role() {
		super(GLMechanics.ROLES);
	}

	public Component getName() {
		return getDesc();
	}

	public void advance(Player player, double v, int point) {
		//TODO
	}

}
