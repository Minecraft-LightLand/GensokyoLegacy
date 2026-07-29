package dev.xkmc.gensokyolegacy.content.quest.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record QuestData(
		List<QuestCondition<?>> conditions,
		Map<String, QuestRequirement<?>> requirements,
		List<QuestReward<?>> rewards,
		Optional<QuestRecurrence> recurrence
) {

	public static final MapCodec<QuestData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			QuestRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(QuestData::conditions),
			Codec.unboundedMap(Codec.STRING, QuestRegistry.REQUIREMENT.codec()).fieldOf("requirements").forGetter(QuestData::requirements),
			QuestRegistry.REWARD.codec().listOf().fieldOf("rewards").forGetter(QuestData::rewards),
			QuestRecurrence.CODEC.optionalFieldOf("recurrence").forGetter(QuestData::recurrence)
	).apply(i, QuestData::new));

}
