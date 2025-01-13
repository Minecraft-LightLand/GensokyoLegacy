package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleStage;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record PlayerRoleHolder(Player player, PlayerRolePlayAttachment root, Role role, PlayerRoleData data) {

	public void advance(int max, int val) {
		if (!(player instanceof ServerPlayer sp))
			return;
		boolean canAdvance = true;
		for (var e : root.getAll(player)) {
			if (e.role != role) {
				if (e.role.getCategory() != role.getCategory())
					canAdvance = false;
				e.data().retract(val);
			}
		}
		if (canAdvance)
			data.advance(max, val);
		root.cleanUp();
		GLMeta.ABILITY.type().network.toClient(sp);
	}

	public RoleStage toStage() {
		return new RoleStage(role(), data().getProgress() / 500);
	}

}
