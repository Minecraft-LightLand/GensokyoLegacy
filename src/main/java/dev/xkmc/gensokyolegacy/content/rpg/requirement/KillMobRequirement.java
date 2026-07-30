package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

public record KillMobRequirement(
		EntityType<?> mob, int count
) implements QuestRequirement<KillMobRequirement> {

	public static final MapCodec<KillMobRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("mob").forGetter(KillMobRequirement::mob),
			Codec.INT.fieldOf("count").forGetter(KillMobRequirement::count)
	).apply(i, KillMobRequirement::new));

	@Override
	public MapCodec<KillMobRequirement> codec() {
		return CODEC;
	}

	@Override
	public int getMaxProgress() {
		return count;
	}

}
