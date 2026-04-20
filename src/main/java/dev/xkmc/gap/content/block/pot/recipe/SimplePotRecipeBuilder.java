package dev.xkmc.gap.content.block.pot.recipe;

import dev.xkmc.gap.content.block.pot.PotHeatState;
import dev.xkmc.gap.content.block.pot.PotRecipeTriggerType;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.fluids.FluidStack;

public class SimplePotRecipeBuilder extends BaseRecipeBuilder<SimplePotRecipeBuilder, SimplePotRecipe, PotRecipe<?>, PotRecipeInput> {

	public SimplePotRecipeBuilder(PotHeatState heat) {
		super(GapRegistries.RS_POT.get(), Items.AIR);
		recipe.heat = heat;
	}

	public SimplePotRecipeBuilder stir() {
		recipe.trigger = PotRecipeTriggerType.STIR;
		return this;
	}

	public SimplePotRecipeBuilder boil(int duration) {
		recipe.trigger = PotRecipeTriggerType.NONE;
		recipe.duration = duration;
		return this;
	}

	public SimplePotRecipeBuilder item(Ingredient ing) {
		recipe.itemInput.add(ing);
		return this;
	}

	public SimplePotRecipeBuilder fluid(PotFluidIngredient ing) {
		recipe.fluidInput.add(ing);
		return this;
	}

	public SimplePotRecipeBuilder output(ItemStack stack) {
		recipe.itemOutput.add(stack);
		return this;
	}

	public SimplePotRecipeBuilder output(FluidStack stack) {
		recipe.fluidOutput.add(stack);
		return this;
	}

}
