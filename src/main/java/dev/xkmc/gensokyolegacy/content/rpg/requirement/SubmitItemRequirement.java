package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.EmptyTrigger;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.util.InventoryMapper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SubmitItemRequirement(
		List<IngredientEntry> ingredients
) implements QuestRequirement<SubmitItemRequirement, EmptyTrigger> {

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

	public static final MapCodec<SubmitItemRequirement> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IngredientEntry.CODEC.listOf().fieldOf("ingredients").forGetter(SubmitItemRequirement::ingredients)
	).apply(i, SubmitItemRequirement::new));

	@Override
	public MapCodec<SubmitItemRequirement> codec() {
		return CODEC;
	}

	@Override
	public Class<EmptyTrigger> getTrigger() {
		return EmptyTrigger.class;
	}

	@Override
	public boolean canComplete(ServerPlayer sp) {
		return InventoryMapper.testCached(sp, this);
	}

	@Override
	public void doComplete(ServerPlayer sp) {
		var ans = new InventoryMapper(sp.getInventory().items, ingredients);
		ans.test();
		ans.consume();
	}

	@Override
	public List<Component> getDesc(Player player, int progress) {
		List<Component> ans = new ArrayList<>();
		for (var e : ingredients) {
			ans.add(e.getDesc(player));
		}
		if (ingredients.size() > 1) {
			boolean pass = InventoryMapper.testCached(player, this);
			ans.addFirst(pass ? GLLang.QUEST$ITEM_SUBMIT_PASS.get() : GLLang.QUEST$ITEM_SUBMIT_FAIL.get());
		}
		return ans;
	}

}
