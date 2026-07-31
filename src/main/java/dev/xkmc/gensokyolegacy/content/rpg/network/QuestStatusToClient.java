package dev.xkmc.gensokyolegacy.content.rpg.network;

import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestData;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record QuestStatusToClient(
		ResourceLocation id, QuestData data, boolean notification
) implements SerialPacketBase<QuestStatusToClient> {

	public QuestStatusToClient(ResourceLocation id, QuestData data) {
		this(id, data, false);
	}

	@Override
	public void handle(Player player) {
		GLMeta.QUEST.type().getOrCreate(player).replace(id, data);
	}

}
