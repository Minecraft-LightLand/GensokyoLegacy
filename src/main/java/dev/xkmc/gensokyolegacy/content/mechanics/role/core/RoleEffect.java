package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRoleHolder;

public interface RoleEffect {

	default void start(PlayerRoleHolder player, int stage) {
	}

	default void tick(PlayerRoleHolder player, int stage) {
	}

	default void end(PlayerRoleHolder player, int stage) {
	}

}
