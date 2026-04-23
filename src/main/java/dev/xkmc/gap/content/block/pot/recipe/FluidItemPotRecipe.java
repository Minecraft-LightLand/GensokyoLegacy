package dev.xkmc.gap.content.block.pot.recipe;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@SerialClass
public class FluidItemPotRecipe<T extends FluidItemPotRecipe<T>> extends PotRecipe<T> {

	@SerialField
	public List<Ingredient> itemInput = new ArrayList<>();

	@SerialField
	public List<PotFluidIngredient> fluidInput = new ArrayList<>();

	@SerialField
	public List<ItemStack> itemOutput = new ArrayList<>();

	@SerialField
	public List<FluidStack> fluidOutput = new ArrayList<>();

	public boolean allowOtherItems = true, allowOtherFluids = true;

	public FluidItemPotRecipe(RecType<T, PotRecipe<?>, PotRecipeInput> fac) {
		super(fac);
	}

	@Override
	public boolean matches(PotRecipeInput input, Level level) {
		if (!super.matches(input, level)) return false;
		// test for invalid fluids
		for (var ing : fluidInput) {
			for (var f : input.be().fluids.getAsList()) {
				if (f.isEmpty()) continue;
				if (ing.fluid().test(f) && f.getAmount() > ing.max())
					return false;
			}
		}
		for (var f : fluidOutput) {
			var fill = input.be().fluids.fill(f, IFluidHandler.FluidAction.SIMULATE);
			if (fill != f.getAmount()) return false;
		}
		// test for valid fluids
		Set<Fluid> containedFluid = new LinkedHashSet<>();
		List<FluidStack> availableFluids = new ArrayList<>();
		for (var f : input.be().fluids.getAsList()) {
			if (f.isEmpty()) continue;
			containedFluid.add(f.getFluid());
			availableFluids.add(f.copy());
		}
		for (var ing : fluidInput) {
			int toDrain = ing.min();
			for (var f : availableFluids) {
				if (ing.fluid().test(f)) {
					containedFluid.remove(f.getFluid());
					if (toDrain > 0) {
						int drain = Math.min(toDrain, f.getAmount());
						f.shrink(drain);
						toDrain -= drain;
					}
				}
			}
			// some ingredient has insufficient fluid
			if (toDrain > 0) return false;
		}
		if (!allowOtherFluids && !containedFluid.isEmpty()) return false;

		Set<Item> containedItems = new LinkedHashSet<>();
		List<ItemStack> availableItems = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			var stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			containedItems.add(stack.getItem());
			availableItems.add(stack.copy());
		}
		for (var ing : itemInput) {
			boolean found = false;
			for (var stack : availableItems) {
				if (ing.test(stack)) {
					containedItems.remove(stack.getItem());
					if (!found) {
						stack.shrink(1);
						found = true;
					}
				}
			}
			// some ingredient not matched
			if (!found) return false;
		}
		return allowOtherItems || containedItems.isEmpty();
	}

	@Override
	public RecipeProgressData start(PotRecipeInput input, HolderLookup.Provider pvd) {
		// test for valid fluids
		List<FluidStack> availableFluids = new ArrayList<>();
		ArrayList<FluidStack> consumedFluids = new ArrayList<>();
		for (var f : input.be().fluids.getAsList()) {
			if (f.isEmpty()) continue;
			availableFluids.add(f);
		}
		for (var ing : fluidInput) {
			int toDrain = ing.cost();
			for (var f : availableFluids) {
				if (ing.fluid().test(f)) {
					if (toDrain > 0) {
						int drain = Math.min(toDrain, f.getAmount());
						consumedFluids.add(f.copyWithAmount(drain));
						f.shrink(drain);
						toDrain -= drain;
					}
				}
			}
		}
		List<ItemStack> availableItems = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			var stack = input.getItem(i);
			if (stack.isEmpty()) continue;
			availableItems.add(stack);
		}
		ArrayList<ItemStack> consumedItems = new ArrayList<>();
		for (var ing : itemInput) {
			boolean found = false;
			for (var stack : availableItems) {
				if (ing.test(stack)) {
					if (!found) {
						consumedItems.add(stack.copyWithCount(1));
						stack.shrink(1);
						found = true;
					}
				}
			}
		}
		input.be().notifyTile();
		return new RecipeProgressData(consumedItems, consumedFluids, new ArrayList<>(itemOutput), new ArrayList<>(fluidOutput));
	}
}
