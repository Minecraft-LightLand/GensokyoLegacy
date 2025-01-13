package dev.xkmc.gensokyolegacy.content.mechanics.ritual;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import net.minecraft.core.Holder;

public class StartRoleRitual extends Ritual {

	private final Holder<Role> role;

	public StartRoleRitual(Holder<Role> role) {
		this.role = role;
	}

}
