package dev.xkmc.gensokyolegacy.content.mechanics.role;

import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class Role extends NamedEntry<Role> {

	private final RoleCategory category;

	public Role(RoleCategory category) {
		super(GLMechanics.ROLES);
		this.category = category;
	}

	public Component getName() {
		return getDesc();
	}

	public void startOrAdvance(Player player, double max, int point) {
		GLMeta.ABILITY.type().getOrCreate(player).getOrCreate(player, this)
				.data().advance(max, point);

	}

	public RoleCategory getCategory() {
		return category;
	}
}
