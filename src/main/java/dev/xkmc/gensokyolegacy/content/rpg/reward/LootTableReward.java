package dev.xkmc.gensokyolegacy.content.rpg.reward;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;
import net.minecraft.resources.ResourceLocation;

public record LootTableReward(ResourceLocation table) implements QuestReward<LootTableReward> {

	public static final MapCodec<LootTableReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("table").forGetter(LootTableReward::table)
	).apply(i, LootTableReward::new));

	@Override
	public MapCodec<LootTableReward> codec() {
		return CODEC;
	}

}
