package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.FixStage;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.PerformanceConstants;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2core.events.SchedulerHandler;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record StructureRepairToServer(
		StructureKey key,
		FixStage stage
) implements SerialPacketBase<StructureRepairToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var home = HomeHolder.of(sp.serverLevel(), key);
		if (home == null) return;
		if (stage == FixStage.ALL) {
			SchedulerHandler.schedulePersistent(() -> home.doFix(PerformanceConstants.COMMAND_PLACE_STEP, stage) == 0);
		} else {
			home.doFix(PerformanceConstants.COMMAND_PLACE_ONCE, stage);
		}
	}

}
