package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.block.censer.AbstractCenserRecipe;
import dev.xkmc.gensokyolegacy.content.block.censer.CenserItemContainer;
import dev.xkmc.gensokyolegacy.content.block.censer.SimpleCenserRecipe;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GLRecipes {

	private static final SR<RecipeType<?>> RT = SR.of(GensokyoLegacy.REG, Registries.RECIPE_TYPE);
	public static final Val<RecipeType<AbstractCenserRecipe<?>>> RT_CENSER = RT.reg("censer", RecipeType::simple);

	private static final SR<RecipeSerializer<?>> RS = SR.of(GensokyoLegacy.REG, Registries.RECIPE_SERIALIZER);
	public static final Val<BaseRecipe.RecType<SimpleCenserRecipe, AbstractCenserRecipe<?>, CenserItemContainer>> RS_CENSER =
			RS.reg("simple_censer", () -> new BaseRecipe.RecType<>(SimpleCenserRecipe.class, RT_CENSER));

	public static void register() {

	}

}