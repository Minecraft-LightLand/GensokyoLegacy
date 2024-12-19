package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public record BlockInfoToClient(
		ArrayList<Component> info,
		ArrayList<Component> advanced
) implements SerialPacketBase<BlockInfoToClient> {

	public static BlockInfoToClient empty() {
		return new BlockInfoToClient(new ArrayList<>(), new ArrayList<>());
	}

	public static BlockInfoToClient of(Component info) {
		return new BlockInfoToClient(new ArrayList<>(List.of(info)), new ArrayList<>());
	}

	@Override
	public void handle(Player player) {
		InfoUpdateClientManager.handleBedInfo(this);
	}

}
