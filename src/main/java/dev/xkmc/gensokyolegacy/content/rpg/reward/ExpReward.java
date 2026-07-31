package dev.xkmc.gensokyolegacy.content.rpg.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;
import net.minecraft.server.level.ServerPlayer;

public record ExpReward(int point) implements QuestReward<ExpReward> {

	public static final MapCodec<ExpReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Codec.INT.fieldOf("point").forGetter(ExpReward::point)
	).apply(i, ExpReward::new));

	@Override
	public MapCodec<ExpReward> codec() {
		return CODEC;
	}

	@Override
	public void execute(ServerPlayer sp, YoukaiEntity ch) {
		sp.giveExperiencePoints(point);
	}

}
