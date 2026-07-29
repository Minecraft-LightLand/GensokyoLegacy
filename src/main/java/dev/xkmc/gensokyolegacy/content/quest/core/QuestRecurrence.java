package dev.xkmc.gensokyolegacy.content.quest.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record QuestRecurrence(
		int cooldown
) {

	public static final Codec<QuestRecurrence> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("cooldown").forGetter(QuestRecurrence::cooldown)
	).apply(i, QuestRecurrence::new));

}
