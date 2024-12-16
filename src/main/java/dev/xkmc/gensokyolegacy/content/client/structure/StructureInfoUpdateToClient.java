package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;

public record StructureInfoUpdateToClient(
		StructureKey key,
		int remove,
		int primary,
		int secondary
) implements SerialPacketBase<StructureInfoUpdateToClient> {

	@Override
	public void handle(Player player) {
		StructureInfoClientManager.setInfo(this);
	}

}
