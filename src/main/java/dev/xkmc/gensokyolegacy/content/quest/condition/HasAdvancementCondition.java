package dev.xkmc.gensokyolegacy.content.quest.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.quest.core.QuestCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record HasAdvancementCondition(
		ResourceLocation advancement
) implements QuestCondition<HasAdvancementCondition> {

	public static final MapCodec<HasAdvancementCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("advancement").forGetter(HasAdvancementCondition::advancement)
	).apply(i, HasAdvancementCondition::new));

	@Override
	public MapCodec<HasAdvancementCondition> codec() {
		return CODEC;
	}

	@Override
	public boolean test(ServerPlayer pl, YoukaiEntity ch) {
		var server = pl.level().getServer();
		if (server == null) return false;
		var holder = server.getAdvancements().get(advancement);
		if (holder == null) return false;
		return pl.getAdvancements().getOrStartProgress(holder).isDone();
	}

}
