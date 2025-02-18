package dev.xkmc.gensokyolegacy.content.block.ritual;

import dev.xkmc.gensokyolegacy.init.registrate.GLRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class SimpleRitualRecipe extends RitualRecipe<SimpleRitualRecipe> {

	@SerialField
	public final List<Ingredient> itemInput = new ArrayList<>();

	@SerialField
	public final List<Ingredient> totemInput = new ArrayList<>();

	@SerialField
	public int mana;

	@SerialField
	public ItemStack result;

	public SimpleRitualRecipe() {
		super(GLRecipes.RS_RITUAL.get());
	}

	@Override
	public boolean matches(RitualInput input, Level level) {
		if (input.blocks().mana() < mana) return false;
		List<ItemStack> inputItems = new ArrayList<>();
		for (int i = 0; i < input.cont().getContainerSize(); i++) {
			ItemStack stack = input.cont().getItem(i);
			if (!stack.isEmpty()) inputItems.add(stack);
		}
		if (RecipeMatcher.findMatches(inputItems, itemInput) == null)
			return false;
		List<ItemStack> inputBlocks = new ArrayList<>();
		for (var state : input.blocks().totems()) {
			ItemStack stack = state.getBlock().asItem().getDefaultInstance();
			if (!stack.isEmpty()) inputItems.add(stack);
		}
		return RecipeMatcher.findMatches(inputBlocks, totemInput) != null;
	}

	@Override
	public ItemStack assemble(RitualInput ritualInput, HolderLookup.Provider provider) {
		return result.copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return result;
	}
}
