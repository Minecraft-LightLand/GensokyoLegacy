package dev.xkmc.gensokyolegacy.content.rpg.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFileCodec;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Quest(
		List<QuestCondition<?>> conditions,
		Map<String, QuestRequirement<?>> requirements,
		List<QuestReward<?>> rewards,
		Optional<QuestRecurrence> recurrence
) {

	public static final Codec<Quest> CODEC = RecordCodecBuilder.create(i -> i.group(
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(Quest::conditions),
			Codec.unboundedMap(Codec.STRING, CodecRegistry.REQUIREMENT.codec()).fieldOf("requirements").forGetter(Quest::requirements),
			CodecRegistry.REWARD.codec().listOf().fieldOf("rewards").forGetter(Quest::rewards),
			QuestRecurrence.CODEC.optionalFieldOf("recurrence").forGetter(Quest::recurrence)
	).apply(i, Quest::new));

	public static final Codec<Holder<Quest>> HOLDER = RegistryFileCodec.create(CodecRegistry.QUEST, CODEC);


}
