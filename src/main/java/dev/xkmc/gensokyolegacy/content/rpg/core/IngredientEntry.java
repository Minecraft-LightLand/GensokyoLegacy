package dev.xkmc.gensokyolegacy.content.rpg.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Optional;

public record IngredientEntry(Ingredient ingredient, int count, Optional<String> text) {

	public static final Codec<IngredientEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
			Ingredient.MAP_CODEC_NONEMPTY.forGetter(IngredientEntry::ingredient),
			Codec.INT.fieldOf("count").forGetter(IngredientEntry::count),
			Codec.STRING.optionalFieldOf("text").forGetter(IngredientEntry::text)
	).apply(i, IngredientEntry::new));

	public Component getDesc(Player player) {
		int has = 0;
		for (var e : player.getInventory().items) {
			if (e.isEmpty()) continue;
			if (ingredient.test(e))
				has += e.getCount();
		}
		if (has > count) has = count;
		var item = text().map(Component::literal).orElse(ingredient.getItems()[0].getHoverName().copy());

		return Component.literal("- ").append(item).append(": ")
				.append(Component.literal("" + has).withStyle(has == count ? ChatFormatting.GREEN : ChatFormatting.RED))
				.append("/").append(Component.literal("" + count).withStyle(ChatFormatting.AQUA));

	}
}
