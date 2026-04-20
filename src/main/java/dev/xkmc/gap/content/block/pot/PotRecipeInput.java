package dev.xkmc.gap.content.block.pot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record PotRecipeInput(PotBlockEntity be, PotHeatState heat,
							 PotRecipeTriggerType trigger) implements RecipeInput {

	@Override
	public ItemStack getItem(int i) {
		return be.items.getItem(i);
	}

	@Override
	public int size() {
		return be.items.getContainerSize();
	}

	@Override
	public boolean isEmpty() {
		return !be.items.isEmpty() && !be.fluids.isEmpty();
	}

}
