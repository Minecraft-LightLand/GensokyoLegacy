package dev.xkmc.gensokyolegacy.content.rpg.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.core.GatedEntry;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Quest(
		EntityType<?> character,
		Holder<Dialog> initialDialog,
		Holder<Dialog> followUpDialog,
		Holder<Dialog> completionDialog,
		List<QuestCondition<?>> conditions,
		Map<String, QuestRequirement<?>> requirements,
		List<QuestReward<?>> rewards,
		Optional<QuestRecurrence> recurrence
) implements GatedEntry {

	public static final Codec<Quest> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("character").forGetter(Quest::character),
			Dialog.HOLDER.fieldOf("initialDialog").forGetter(Quest::initialDialog),
			Dialog.HOLDER.fieldOf("followUpDialog").forGetter(Quest::followUpDialog),
			Dialog.HOLDER.fieldOf("completionDialog").forGetter(Quest::completionDialog),
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(Quest::conditions),
			Codec.unboundedMap(Codec.STRING, CodecRegistry.REQUIREMENT.codec()).fieldOf("requirements").forGetter(Quest::requirements),
			CodecRegistry.REWARD.codec().listOf().fieldOf("rewards").forGetter(Quest::rewards),
			QuestRecurrence.CODEC.optionalFieldOf("recurrence").forGetter(Quest::recurrence)
	).apply(i, Quest::new));

	public static final Codec<Holder<Quest>> HOLDER = RegistryFileCodec.create(CodecRegistry.QUEST.key(), CODEC);

}
