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
		List<QuestCondition<?>> conditions,
		String title,
		String description,
		Optional<QuestRecurrence> recurrence,
		Map<String, QuestRequirement<?, ?>> requirements,
		List<QuestReward<?>> rewards,
		DialogOption<?> initialDialog,
		DialogOption<?> followUpDialog,
		DialogOption<?> completionDialog
) implements GatedEntry {

	public static final Codec<Quest> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("character").forGetter(Quest::character),
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(Quest::conditions),
			Codec.STRING.fieldOf("title").forGetter(Quest::title),
			Codec.STRING.fieldOf("description").forGetter(Quest::description),
			QuestRecurrence.CODEC.optionalFieldOf("recurrence").forGetter(Quest::recurrence),
			Codec.unboundedMap(Codec.STRING, CodecRegistry.REQUIREMENT.codec()).fieldOf("requirements").forGetter(Quest::requirements),
			CodecRegistry.REWARD.codec().listOf().fieldOf("rewards").forGetter(Quest::rewards),
			CodecRegistry.OPTION.codec().fieldOf("initialDialog").forGetter(Quest::initialDialog),
			CodecRegistry.OPTION.codec().fieldOf("followUpDialog").forGetter(Quest::followUpDialog),
			CodecRegistry.OPTION.codec().fieldOf("completionDialog").forGetter(Quest::completionDialog)
	).apply(i, Quest::new));

	public static final Codec<Holder<Quest>> HOLDER = RegistryFileCodec.create(CodecRegistry.Keys.QUEST, CODEC);

}
