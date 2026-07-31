package dev.xkmc.gensokyolegacy.content.rpg.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.trigger.EmptyTrigger;
import dev.xkmc.gensokyolegacy.util.Matcher;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public record SubmitItemRequirement(
		List<IngredientEntry> ingredients
) implements QuestRequirement<SubmitItemRequirement, EmptyTrigger> {

	public record IngredientEntry(Ingredient ingredient, int count) {

		public static final Codec<IngredientEntry> CODEC = RecordCodecBuilder.create(i -> i.group(
				Ingredient.MAP_CODEC_NONEMPTY.forGetter(IngredientEntry::ingredient),
				Codec.INT.fieldOf("count").forGetter(IngredientEntry::count)
		).apply(i, IngredientEntry::new));
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
		return new InventoryMapper(sp.getInventory().items, ingredients).test();
	}

	@Override
	public void doComplete(ServerPlayer sp) {
		var ans = new InventoryMapper(sp.getInventory().items, ingredients);
		ans.test();
		ans.consume();
	}

	public static class InventoryMapper {

		private static class Sink {

			private final Ingredient ingredient;
			private final int required;

			public Sink(IngredientEntry entry) {
				this.ingredient = entry.ingredient();
				this.required = entry.count();
			}

		}

		private static class Source {

			private final ItemStack stack;
			private final boolean[] map;

			public Source(ItemStack input, boolean[] map) {
				this.stack = input;
				this.map = map;
			}
		}

		private final ItemStack[] inputs;
		private final Sink[] sinks;

		private Source[] source;
		private int[][] ans;

		public InventoryMapper(List<ItemStack> inputs, List<IngredientEntry> sinks) {
			this.inputs = inputs.toArray(ItemStack[]::new);
			this.sinks = new Sink[sinks.size()];
			for (int i = 0; i < this.sinks.length; i++)
				this.sinks[i] = new Sink(sinks.get(i));
		}

		public boolean test() {
			List<Source> stacks = new ArrayList<>();
			for (ItemStack input : inputs) {
				if (input.isEmpty()) continue;
				boolean[] map = new boolean[sinks.length];
				int validUse = 0;
				for (int j = 0; j < sinks.length; j++) {
					map[j] = sinks[j].ingredient.test(input);
					validUse++;
				}
				if (validUse > 0)
					stacks.add(new Source(input, map));
			}
			source = stacks.toArray(Source[]::new);

			int[] items = new int[source.length];
			for (int i = 0; i < source.length; i++)
				items[i] = source[i].stack.getCount();
			Matcher.Req[] reqs = new Matcher.Req[sinks.length];
			for (int i = 0; i < reqs.length; i++) {
				boolean[] remap = new boolean[source.length];
				for (int j = 0; j < source.length; j++)
					remap[j] = source[j].map[i];
				reqs[i] = new Matcher.Req(sinks[i].required, remap);
			}
			ans = Matcher.solve(items, reqs);
			return ans != null;
		}

		public void consume() {
			for (int i = 0; i < source.length; i++) {
				int sum = 0;
				for (int j = 0; j < sinks.length; j++) {
					sum += ans[i][j];
				}
				source[i].stack.shrink(sum);
			}
		}

	}

}
