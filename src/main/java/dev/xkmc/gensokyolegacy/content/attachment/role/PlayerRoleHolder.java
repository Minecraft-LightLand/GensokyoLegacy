package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.Role;
import net.minecraft.world.entity.player.Player;

public record PlayerRoleHolder(Player player, PlayerRolePlayAttachment root, Role role, PlayerRoleData data) {

	public void advance(double max, int val) {
		boolean canAdvance = true;
		for (var e : root.getAll(player)) {
			if (e.role != role) {
				if (e.role.getCategory() != role.getCategory())
					canAdvance = false;
				e.data().retract(val);
			}
		}
		root.cleanUp();
		if (canAdvance)
			data.advance(max, val);
	}

}
