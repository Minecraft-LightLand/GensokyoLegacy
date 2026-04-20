package dev.xkmc.gap.content.block.pot.recipe;

import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public record PotFluidIngredient(FluidIngredient fluid, int min, int max) {
}
