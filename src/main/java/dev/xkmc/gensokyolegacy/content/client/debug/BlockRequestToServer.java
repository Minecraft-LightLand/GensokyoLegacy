package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record BlockRequestToServer(BlockPos pos) implements SerialPacketBase<BlockRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		if (!player.level().isLoaded(pos)) return;
		if (!(player.level().getBlockEntity(pos) instanceof IDebugInfoBlockEntity be)) return;
		GensokyoLegacy.HANDLER.toClientPlayer(be.getDebugPacket(sp), sp);
	}

}
