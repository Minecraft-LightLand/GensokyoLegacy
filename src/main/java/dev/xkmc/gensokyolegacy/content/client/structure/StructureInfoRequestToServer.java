package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record StructureInfoRequestToServer(
		StructureKey key
) implements SerialPacketBase<StructureInfoRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var home = HomeHolder.of(sp.serverLevel(), key);
		if (home == null || !home.isValid()) return;
		var packet = home.data().getAbnormality(key);
		GensokyoLegacy.HANDLER.toClientPlayer(packet, sp);
	}

}
