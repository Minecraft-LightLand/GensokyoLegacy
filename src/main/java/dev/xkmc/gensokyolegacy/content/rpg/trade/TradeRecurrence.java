package dev.xkmc.gensokyolegacy.content.rpg.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record TradeRecurrence(int maxStock, int restockTime) {

	public static final Codec<TradeRecurrence> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("maxStock").forGetter(TradeRecurrence::maxStock),
			Codec.INT.fieldOf("restockTime").forGetter(TradeRecurrence::restockTime)
	).apply(i, TradeRecurrence::new));

}
