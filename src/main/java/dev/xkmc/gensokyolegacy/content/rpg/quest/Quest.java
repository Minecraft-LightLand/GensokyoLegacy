package dev.xkmc.gensokyolegacy.content.rpg.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Quest(
		EntityType<?> character,
		DialogOption<?> initialDialog,
		DialogOption<?> followUpDialog,
		DialogOption<?> completionDialog,
		List<QuestCondition<?>> conditions,
		Map<String, QuestRequirement<?>> requirements,
		List<QuestReward<?>> rewards,
		Optional<QuestRecurrence> recurrence
) implements GatedEntry {

	public static final Codec<Quest> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("character").forGetter(Quest::character),
			CodecRegistry.OPTION.codec().fieldOf("initialDialog").forGetter(Quest::initialDialog),
			CodecRegistry.OPTION.codec().fieldOf("followUpDialog").forGetter(Quest::followUpDialog),
			CodecRegistry.OPTION.codec().fieldOf("completionDialog").forGetter(Quest::completionDialog),
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(Quest::conditions),
			Codec.unboundedMap(Codec.STRING, CodecRegistry.REQUIREMENT.codec()).fieldOf("requirements").forGetter(Quest::requirements),
			CodecRegistry.REWARD.codec().listOf().fieldOf("rewards").forGetter(Quest::rewards),
			QuestRecurrence.CODEC.optionalFieldOf("recurrence").forGetter(Quest::recurrence)
	).apply(i, Quest::new));

	public static final Codec<Holder<Quest>> HOLDER = RegistryFileCodec.create(CodecRegistry.QUEST.key(), CODEC);

}
