package dev.xkmc.gensokyolegacy.content.rpg.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;

public record ReputationReward(int reputation) implements QuestReward<ReputationReward> {

	public static final MapCodec<ReputationReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("reputation").forGetter(ReputationReward::reputation)
	).apply(i, ReputationReward::new));

	@Override
	public MapCodec<ReputationReward> codec() {
		return CODEC;
	}

}
