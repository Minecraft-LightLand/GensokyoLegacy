package dev.xkmc.gensokyolegacy.content.rpg.quest;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

import java.util.TreeMap;

@SerialClass
public class QuestData {

	@SerialField
	public int completed;

	@SerialField
	public long lastCompletion;

	@SerialField
	public final TreeMap<String, Integer> progress = new TreeMap<>();

}
