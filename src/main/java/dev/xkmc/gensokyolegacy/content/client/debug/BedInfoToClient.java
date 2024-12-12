package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record BedInfoToClient(
		@Nullable StructureKey key,
		@Nullable BlockPos entityPos,
		long lastEntityTime,
		long respawnTime
) implements SerialPacketBase<BedInfoToClient> {

	@Override
	public void handle(Player player) {
		InfoUpdateClientManager.handleBedInfo(this);
	}

}
