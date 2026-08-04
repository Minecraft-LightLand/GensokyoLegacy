package dev.xkmc.gensokyolegacy.content.rpg.trade;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.core.*;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record TradeOffer(
		EntityType<?> character,
		List<QuestCondition<?>> conditions,
		ItemStack result,
		TradeRecurrence recurrence,
		List<IngredientEntry> ingredients
) implements GatedEntry, CharacterEntry, IngredientList {

	public static final Codec<TradeOffer> CODEC = RecordCodecBuilder.create(i -> i.group(
			BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("character").forGetter(TradeOffer::character),
			CodecRegistry.CONDITION.codec().listOf().fieldOf("conditions").forGetter(TradeOffer::conditions),
			ItemStack.CODEC.fieldOf("result").forGetter(TradeOffer::result),
			TradeRecurrence.CODEC.fieldOf("recurrence").forGetter(TradeOffer::recurrence),
			IngredientEntry.CODEC.listOf().fieldOf("ingredients").forGetter(TradeOffer::ingredients)
	).apply(i, TradeOffer::new));

	public static final Codec<Holder<TradeOffer>> HOLDER = RegistryFileCodec.create(CodecRegistry.Keys.TRADE, CODEC);

}
