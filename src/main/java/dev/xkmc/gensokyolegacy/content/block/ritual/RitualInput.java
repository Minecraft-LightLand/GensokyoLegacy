package dev.xkmc.gensokyolegacy.content.block.ritual;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record RitualInput(RitualItemContainer cont, RitualResult blocks) implements RecipeInput {
	@Override
	public ItemStack getItem(int index) {
		return cont.getItem(index);
	}

	@Override
	public int size() {
		return cont.getContainerSize();
	}
}
