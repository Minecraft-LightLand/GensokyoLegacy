package dev.xkmc.gensokyolegacy.content.attachment.misc;

import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public record FrogSyncPacket(boolean hasHat, int id) implements SerialPacketBase<FrogSyncPacket> {

	public FrogSyncPacket(LivingEntity entity, FrogGodCapability cap) {
		this(cap.hasHat, entity.getId());
	}

	@Override
	public void handle(Player player) {
		ClientCapHandler.frogUpdate(this);
	}
}
