package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record BedRequestToServer(BlockPos pos) implements SerialPacketBase<BedRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		if (!player.level().isLoaded(pos)) return;
		if (!(player.level().getBlockEntity(pos) instanceof YoukaiBedBlockEntity be)) return;
		GensokyoLegacy.HANDLER.toClientPlayer(be.getDebugPacket(), sp);
	}

}
