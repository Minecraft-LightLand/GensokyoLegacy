package dev.xkmc.alchemy.init;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.gap.content.block.pot.PotHeatState;
import dev.xkmc.gap.content.block.pot.recipe.PotFluidIngredient;
import dev.xkmc.gap.content.block.pot.recipe.SimplePotRecipeBuilder;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

public class AlchemyDataGen {

	public static void dataMap(RegistrateDataMapProvider pvd) {
		AlchemyRegistries.PYRO.regDataMap(pvd);
		AlchemyRegistries.NATURE.regDataMap(pvd);
		AlchemyRegistries.EARTH.regDataMap(pvd);
	}

	public static void recipe(RegistrateRecipeProvider pvd) {
		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.WHEAT))
				.output(new FluidStack(AlchemyRegistries.NATURE.getPos(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.IRON_NUGGET))
				.output(new FluidStack(AlchemyRegistries.NATURE.getNeg(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.CLAY))
				.output(new FluidStack(AlchemyRegistries.EARTH.getPos(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.ENDER_PEARL))
				.output(new FluidStack(AlchemyRegistries.EARTH.getNeg(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.BLAZE_POWDER))
				.output(new FluidStack(AlchemyRegistries.PYRO.getPos(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(Fluids.WATER), 250, 1000, 0))
				.item(Ingredient.of(Items.PRISMARINE_SHARD))
				.output(new FluidStack(AlchemyRegistries.PYRO.getNeg(), 1));

		GLRecipeGen.unlock(pvd, new SimplePotRecipeBuilder(PotHeatState.NONE)::unlockedBy, Items.STICK)
				.fluid(new PotFluidIngredient(FluidIngredient.of(AlchemyRegistries.PYRO.getPos().value()), 1, 1000, 1))
				.fluid(new PotFluidIngredient(FluidIngredient.of(AlchemyRegistries.NATURE.getNeg().value()), 1, 1000, 1))
				.item(Ingredient.of(Items.APPLE))
				.output(Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());

	}

}
