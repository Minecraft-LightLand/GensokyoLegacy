package dev.xkmc.gensokyolegacy.content.rpg.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;

public record ReputationReward(int reputation, int max) implements QuestReward<ReputationReward> {

	public static final MapCodec<ReputationReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("reputation").forGetter(ReputationReward::reputation),
			Codec.INT.fieldOf("max").forGetter(ReputationReward::max)

	).apply(i, ReputationReward::new));

	@Override
	public MapCodec<ReputationReward> codec() {
		return CODEC;
	}

	@Override
	public void execute(ServerPlayer sp, YoukaiEntity ch) {
		GLMeta.CHAR.type().getOrCreate(sp).get(sp, ch).gain(reputation, max);
	}

}
