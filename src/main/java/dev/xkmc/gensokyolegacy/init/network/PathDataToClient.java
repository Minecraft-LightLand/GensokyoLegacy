package dev.xkmc.gensokyolegacy.init.network;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public record PathDataToClient(
		int id, ArrayList<Vec3> pos
) implements SerialPacketBase<PathDataToClient> {

	@Override
	public void handle(Player player) {

	}

}
