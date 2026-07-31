package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.KillTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public record KillMobRequirement(
		EntityPredicate target, int count
) implements QuestRequirement<KillMobRequirement, KillTrigger> {

	public static final MapCodec<KillMobRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			EntityPredicate.CODEC.fieldOf("target").forGetter(KillMobRequirement::target),
			Codec.INT.fieldOf("count").forGetter(KillMobRequirement::count)
	).apply(i, KillMobRequirement::new));

	public KillMobRequirement(EntityType<?> type, int count) {
		this(EntityPredicate.Builder.entity().of(type).build(), count);
	}

	public KillMobRequirement(TagKey<EntityType<?>> type, int count) {
		this(EntityPredicate.Builder.entity().of(type).build(), count);
	}

	@Override
	public MapCodec<KillMobRequirement> codec() {
		return CODEC;
	}

	@Override
	public Class<KillTrigger> getTrigger() {
		return KillTrigger.class;
	}

	@Override
	public boolean match(KillTrigger trigger) {
		return target.matches(trigger.player(), trigger.target());
	}

	@Override
	public int getMaxProgress() {
		return count;
	}

}
