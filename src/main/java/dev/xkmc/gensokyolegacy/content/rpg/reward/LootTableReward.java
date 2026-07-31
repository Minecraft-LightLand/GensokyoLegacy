package dev.xkmc.gensokyolegacy.content.rpg.reward;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record LootTableReward(ResourceLocation table) implements QuestReward<LootTableReward> {

	public static final MapCodec<LootTableReward> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("table").forGetter(LootTableReward::table)
	).apply(i, LootTableReward::new));

	@Override
	public MapCodec<LootTableReward> codec() {
		return CODEC;
	}

	@Override
	public void execute(ServerPlayer sp, YoukaiEntity ch) {
		var loot = sp.serverLevel().getServer().reloadableRegistries()
				.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, table));
		var params = new LootParams.Builder(sp.serverLevel())
				.withParameter(LootContextParams.THIS_ENTITY, sp)
				.withParameter(LootContextParams.ORIGIN, sp.position())
				.create(LootContextParamSets.ADVANCEMENT_REWARD);
		loot.getRandomItems(params, e -> sp.getInventory().placeItemBackInInventory(e));
	}

}
