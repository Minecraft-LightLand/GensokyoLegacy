package dev.xkmc.gensokyolegacy.content.rpg.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;

public record SelfReputationCondition(
		int reputation
) implements QuestCondition<SelfReputationCondition> {

	public static final MapCodec<SelfReputationCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("reputation").forGetter(SelfReputationCondition::reputation)
	).apply(i, SelfReputationCondition::new));

	@Override
	public MapCodec<SelfReputationCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(ServerPlayer pl, YoukaiEntity ch) {
		return GLMeta.CHAR.type().getOrCreate(pl).get(pl, ch).data().reputation >= reputation;
	}

}
