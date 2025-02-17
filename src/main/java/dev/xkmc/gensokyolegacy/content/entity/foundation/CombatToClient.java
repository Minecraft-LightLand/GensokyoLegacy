package dev.xkmc.gensokyolegacy.content.entity.foundation;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record CombatToClient(
		int id, CombatProgress progress
) implements SerialPacketBase<CombatToClient> {

	@Override
	public void handle(Player player) {
		if (player.level().getEntity(id) instanceof DamageClampEntity e) {

		}
	}

}
