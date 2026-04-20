package dev.xkmc.gap.init.registrate;

import dev.xkmc.gap.content.block.pot.recipe.PotRecipe;
import dev.xkmc.gap.content.block.pot.PotRecipeInput;
import dev.xkmc.gap.content.block.pot.recipe.SimplePotRecipe;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GapRegistries {

	private static final SR<RecipeType<?>> RT = SR.of(GensokyoLegacy.REG, Registries.RECIPE_TYPE);
	public static final Val<RecipeType<PotRecipe<?>>> RT_POT = RT.reg("pot", RecipeType::simple);

	private static final SR<RecipeSerializer<?>> RS = SR.of(GensokyoLegacy.REG, Registries.RECIPE_SERIALIZER);
	public static final Val<BaseRecipe.RecType<SimplePotRecipe, PotRecipe<?>, PotRecipeInput>> RS_POT =
			RS.reg("pot", () -> new BaseRecipe.RecType<>(SimplePotRecipe.class, RT_POT));

	public static void register() {

	}

}
