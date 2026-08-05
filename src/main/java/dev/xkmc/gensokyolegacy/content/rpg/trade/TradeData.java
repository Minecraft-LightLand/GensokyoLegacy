package dev.xkmc.gensokyolegacy.content.rpg.trade;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class TradeData {

	@SerialField
	public int trades;

	@SerialField
	public long timestamp;

}
