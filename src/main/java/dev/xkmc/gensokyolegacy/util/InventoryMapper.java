package dev.xkmc.gensokyolegacy.util;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.rpg.requirement.SubmitItemRequirement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

public class InventoryMapper {

	private static final LinkedHashMap<Pair<UUID, Object>, Pair<Long, Boolean>> CACHE = new LinkedHashMap<>();

	public static boolean testCached(Player player, SubmitItemRequirement req) {
		var uuid = player.getUUID();
		Pair<UUID, Object> key = Pair.of(uuid, req);
		var old = CACHE.get(key);
		if (old != null && old.getFirst() == player.level().getGameTime())
			return old.getSecond();
		boolean ans = new InventoryMapper(player.getInventory().items, req.ingredients()).test();
		CACHE.put(key, Pair.of(player.level().getGameTime(), ans));
		return ans;
	}

	private static class Sink {

		private final Ingredient ingredient;
		private final int required;

		public Sink(SubmitItemRequirement.IngredientEntry entry) {
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

	public InventoryMapper(List<ItemStack> inputs, List<SubmitItemRequirement.IngredientEntry> sinks) {
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
