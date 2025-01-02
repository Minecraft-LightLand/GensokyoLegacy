package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.Incense;
import dev.xkmc.gensokyolegacy.init.registrate.GLRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SimpleCenserRecipeBuilder extends
		BaseRecipeBuilder<SimpleCenserRecipeBuilder, SimpleCenserRecipe, AbstractCenserRecipe<?>, CenserItemContainer> {

	public SimpleCenserRecipeBuilder(Incense result, int duration) {
		super(GLRecipes.RS_CENSER.get(), result.asStack().getItem());
		recipe.result = result;
		recipe.duration = duration;
	}

	public SimpleCenserRecipeBuilder require(Ingredient ing) {
		recipe.ingredients.add(ing);
		return this;
	}

	public SimpleCenserRecipeBuilder require(ItemLike item) {
		return require(Ingredient.of(item));
	}

	public SimpleCenserRecipeBuilder require(TagKey<Item> item) {
		return require(Ingredient.of(item));
	}

}
