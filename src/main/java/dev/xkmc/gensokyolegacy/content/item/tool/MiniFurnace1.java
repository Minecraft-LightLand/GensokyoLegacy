package dev.xkmc.gensokyolegacy.content.item.tool;

import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MiniFurnace1 extends Item implements InvClickItem {

	public enum State {
		OFF(Blocks.AIR), SMOKE(Blocks.SMOKER), FURNACE(Blocks.FURNACE), BLAST(Blocks.BLAST_FURNACE);

		private final Block block;

		State(Block block) {
			this.block = block;
		}

		@Nullable
		public RecipeType<? extends AbstractCookingRecipe> getType() {
			return switch (this) {
				case OFF -> null;
				case SMOKE -> RecipeType.SMOKING;
				case FURNACE -> RecipeType.SMELTING;
				case BLAST -> RecipeType.BLASTING;
			};
		}

		@Nullable
		public RecipeHolder<? extends AbstractCookingRecipe> getRecipe(ServerPlayer sp, SingleRecipeInput inv) {
			var rm = sp.serverLevel().getRecipeManager();
			return switch (this) {
				case OFF -> null;
				case SMOKE -> rm.getRecipeFor(RecipeType.SMOKING, inv, sp.level()).orElse(null);
				case FURNACE -> rm.getRecipeFor(RecipeType.SMELTING, inv, sp.level()).orElse(null);
				case BLAST -> rm.getRecipeFor(RecipeType.BLASTING, inv, sp.level()).orElse(null);
			};
		}
	}

	public MiniFurnace1(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
		var data = GLItems.DC_FURNACE_1.getOrDefault(stack, Data.DEF);
		list.add(GLLang.ITEM$FURNACE_1_LORE.get());
		list.add(GLLang.ITEM$FURNACE_1_USE.get());
		if (data.state() == State.OFF) list.add(GLLang.ITEM$FURNACE_1_OFF.get());
		else list.add(GLLang.ITEM$FURNACE_1_DESC.get(
				Component.translatable(data.state.block.getDescriptionId()).withStyle(ChatFormatting.WHITE)));
	}

	@Override
	public void handleClick(ServerPlayer sp, ItemStack stack) {
		var data = GLItems.DC_FURNACE_1.getOrDefault(stack, Data.DEF);
		stack.set(GLItems.DC_FURNACE_1, data.rotate());
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (slotId < 9 || slotId >= 36) return;
		if (!(entity instanceof ServerPlayer sp)) return;
		if (sp.getInventory().getItem(slotId) != stack) return;
		int r = (slotId - 9) / 9;
		int c = slotId % 9;
		test(sp, stack, 0, r, c - 1, r - 1, c - 1);
		test(sp, stack, 1, r - 1, c, r - 1, c + 1);
		test(sp, stack, 2, r, c + 1, r + 1, c + 1);
		test(sp, stack, 3, r + 1, c, r + 1, c - 1);
	}

	private void test(ServerPlayer sp, ItemStack stack, int index, int r0, int c0, int r1, int c1) {
		if (r0 < 0 || r0 >= 3 || c0 < 0 || c0 >= 9 || r1 < 0 || r1 >= 3 || c1 < 0 || c1 >= 9) return;
		int s0 = r0 * 9 + c0 + 9;
		int s1 = r1 * 9 + c1 + 9;
		var data = GLItems.DC_FURNACE_1.getOrDefault(stack, Data.DEF);
		if (data.data().length != 4) {
			data = new Data(data.state(), new Entry[4]);
		}
		var entry = data.data()[index];
		if (entry == null && data.state().getType() != null) {
			ItemStack in = sp.getInventory().getItem(s0);
			var inv = new SingleRecipeInput(in);
			var rec = data.state.getRecipe(sp, inv);
			if (rec != null) {
				entry = new Entry(rec.id(), 0, rec.value().getCookingTime() * 2);
			}
		}
		if (entry != null) {
			entry = entry.match(sp, data.state(), s0, s1);
		}
		if (entry != data.data()[index]) {
			stack.set(GLItems.DC_FURNACE_1, data.with(index, entry));
		}
	}

	public record Data(State state, Entry[] data) {

		public static final Data DEF = new Data(State.OFF, new Entry[4]);

		public Data with(int index, @Nullable Entry entry) {
			var ans = data.clone();
			ans[index] = entry;
			return new Data(state, ans);
		}

		public Data rotate() {
			var next = State.values()[(state.ordinal() + 1) % 4];
			return new Data(next, data);
		}
	}

	public record Entry(ResourceLocation recipe, int time, int max) {

		@Nullable
		public Entry match(ServerPlayer sp, State state, int s0, int s1) {
			if (recipe == null) return null;
			ItemStack in = sp.getInventory().getItem(s0);
			if (in.isEmpty()) return null;
			if (!in.isStackable() && !in.getComponentsPatch().isEmpty()) return null;
			ItemStack out = sp.getInventory().getItem(s1);
			var opt = sp.serverLevel().getServer().getRecipeManager().byKey(recipe);
			if (opt.isEmpty()) return null;
			var rec = opt.get().value();
			if (!(rec instanceof AbstractCookingRecipe r)) return null;
			if (state.getType() != r.getType()) return null;
			var inv = new SingleRecipeInput(in);
			if (!r.matches(inv, sp.serverLevel())) return null;
			ItemStack res = r.assemble(inv, sp.registryAccess());
			if (!out.isEmpty()) {
				if (!ItemStack.isSameItemSameComponents(res, out)) return null;
				if (res.getCount() + out.getCount() > res.getMaxStackSize()) return null;
			}
			if (time + 1 < max) {
				return new Entry(recipe, time + 1, max);
			}
			if (out.isEmpty()) {
				sp.getInventory().setItem(s1, res);
			} else {
				out.grow(res.getCount());
			}
			in.shrink(1);
			if (in.isEmpty()) return null;
			return new Entry(recipe, 0, max);
		}

	}

}
