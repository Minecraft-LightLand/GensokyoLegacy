package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record CharacterRequestToServer(UUID id) implements SerialPacketBase<CharacterRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		if (!(sp.serverLevel().getEntity(id) instanceof YoukaiEntity e)) return;
		e.getDebugPacket(sp).ifPresent(p -> GensokyoLegacy.HANDLER.toClientPlayer(p, sp));
	}

}
