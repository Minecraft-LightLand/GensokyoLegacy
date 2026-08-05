package dev.xkmc.gensokyolegacy.content.rpg.network;

import dev.xkmc.gensokyolegacy.content.rpg.trade.TradeData;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record TradeStatusToClient(ResourceLocation id, TradeData data) implements SerialPacketBase<TradeStatusToClient> {

	@Override
	public void handle(Player player) {
		GLMeta.TRADE.type().getOrCreate(player).replace(id, data);
	}

}
