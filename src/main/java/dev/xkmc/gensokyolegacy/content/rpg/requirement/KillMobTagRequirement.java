package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import net.minecraft.resources.ResourceLocation;

public record KillMobTagRequirement(
		ResourceLocation tag, int count
) implements QuestRequirement<KillMobTagRequirement> {

	public static final MapCodec<KillMobTagRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("tag").forGetter(KillMobTagRequirement::tag),
			Codec.INT.fieldOf("count").forGetter(KillMobTagRequirement::count)
	).apply(i, KillMobTagRequirement::new));

	@Override
	public MapCodec<KillMobTagRequirement> codec() {
		return CODEC;
	}

	@Override
	public int getMaxProgress() {
		return count;
	}

}
