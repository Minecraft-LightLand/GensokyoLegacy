package dev.xkmc.gensokyolegacy.content.mechanics.role.effect;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;

public interface AttackEffect extends RoleEffect {

	default boolean immune(DamageData.Attack cache, int stage) {
		return false;
	}

	default void onDamage(Role role, DamageData.Defence cache, int stage) {

	}

}
