package dev.xkmc.gensokyolegacy.content.rpg.trade;

import dev.xkmc.gensokyolegacy.content.rpg.network.TradeStatusToClient;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashMap;

@SerialClass
public class TradeAttachment extends PlayerCapabilityTemplate<TradeAttachment> {

	@SerialField
	public final LinkedHashMap<ResourceLocation, TradeData> data = new LinkedHashMap<>();

	public TradeData getData(ResourceLocation id) {
		return data.computeIfAbsent(id, k -> new TradeData());
	}

	public void replace(ResourceLocation id, TradeData val) {
		data.put(id, val);
	}

	public int getRemainingTrades(Player pl, Holder<TradeOffer> offer) {
		var id = offer.unwrapKey().orElseThrow().location();
		var d = getData(id);
		int max = getMaxTrades(offer);
		long time = pl.level().getGameTime();
		if (d.timestamp <= 0) return max;
		if (time < d.timestamp - 20) return max;
		if (time - d.timestamp >= offer.value().recurrence().restockTime()) return max;
		return Math.min(d.trades, max);
	}

	public int getMaxTrades(Holder<TradeOffer> offer) {
		return offer.value().recurrence().maxStock();
	}

	public void onTrade(Player pl, Holder<TradeOffer> offer) {
		var id = offer.unwrapKey().orElseThrow().location();
		var d = getData(id);
		d.trades = Math.max(0, getRemainingTrades(pl, offer) - 1);
		d.timestamp = pl.level().getGameTime();
	}

	public void onTradeAndSync(ServerPlayer sp, Holder<TradeOffer> offer) {
		onTrade(sp, offer);
		var id = offer.unwrapKey().orElseThrow().location();
		GensokyoLegacy.HANDLER.toClientPlayer(new TradeStatusToClient(id, getData(id)), sp);
	}

}
