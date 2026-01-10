package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.home.structure.FixStage;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.IFixableHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.PerformanceConstants;
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
		var home = IHomeHolder.of(sp.serverLevel(), key);
		if (!(home instanceof IFixableHomeHolder fix)) return;
		if (stage == FixStage.ALL) {
			SchedulerHandler.schedulePersistent(() -> fix.doFix(PerformanceConstants.COMMAND_PLACE_STEP, stage) == 0);
		} else {
			fix.doFix(PerformanceConstants.COMMAND_PLACE_ONCE, stage);
		}
	}

}
