package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.gensokyolegacy.init.registrate.GLDecoBlocks;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.BiFunction;

public class GLRecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
			GLDecoBlocks.ICE_SET.genRecipe(pvd);
			GLDecoBlocks.SNOW_SET.genRecipe(pvd);

			pvd.stonecutting(DataIngredient.items(Blocks.PACKED_ICE), RecipeCategory.BUILDING_BLOCKS, GLDecoBlocks.ICE_SET.block);

			GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GLDecoBlocks.SNOW_SET.block.get())::unlockedBy, Items.SNOW_BLOCK)
					.pattern("XX").pattern("XX")
					.define('X', Items.SNOW_BLOCK)
					.save(pvd);
		}

	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

}
