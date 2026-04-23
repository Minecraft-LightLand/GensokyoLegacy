package dev.xkmc.gap.content.block.pot.recipe;

import dev.xkmc.gap.content.block.pot.PotBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;

public record RecipeProgressData(
		ArrayList<ItemStack> consumedItems,
		ArrayList<FluidStack> consumedFluids,
		ArrayList<ItemStack> producedItems,
		ArrayList<FluidStack> producedFluids
) {

	public void execute(PotBlockEntity be) {
		for (var e : producedItems) {
			if (!e.isEmpty()) {
				be.items.addItem(e);
			}
		}
		for (var e : producedFluids) {
			if (!e.isEmpty()) {
				be.fluids.fill(e, IFluidHandler.FluidAction.EXECUTE);
			}
		}
	}

}
