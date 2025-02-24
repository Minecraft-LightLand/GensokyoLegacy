package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.block.censer.SimpleCenserRecipeBuilder;
import dev.xkmc.gensokyolegacy.init.food.GLDish;
import dev.xkmc.gensokyolegacy.init.food.GLFood;
import dev.xkmc.gensokyolegacy.init.food.GLFoodItems;
import dev.xkmc.gensokyolegacy.init.food.GLSake;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.youkaishomecoming.content.pot.base.BasePotOutput;
import dev.xkmc.youkaishomecoming.content.pot.ferment.SimpleFermentationBuilder;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.food.CakeEntry;
import dev.xkmc.youkaishomecoming.init.food.YHFood;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

import java.util.function.BiFunction;

public class GLRecipeGen {

	public static void genRecipe(RegistrateRecipeProvider pvd) {
		{
		}

		// food
		{
			pvd.stonecutting(DataIngredient.items(Items.IRON_INGOT), RecipeCategory.MISC, GLFoodItems.CAN);
			pvd.smelting(DataIngredient.items(GLFoodItems.CAN.get()), RecipeCategory.MISC, Items.IRON_INGOT::asItem, 0.1f, 200);

			food(pvd, GLFood.FLESH, GLFood.COOKED_FLESH);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GLFood.FLESH_ROLL.item, 2)::unlockedBy, GLFood.FLESH.item.get())
					.pattern("FF").pattern("KR")
					.define('F', GLTagGen.RAW_FLESH)
					.define('K', Items.DRIED_KELP)
					.define('R', ModItems.COOKED_RICE.get())
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GLFood.HIGI_CHOCOLATE.item, 3)::unlockedBy, Items.COCOA_BEANS)
					.requires(YHItems.MATCHA).requires(Items.TWISTING_VINES).requires(Items.PINK_PETALS)
					.requires(Items.HONEY_BOTTLE).requires(Items.BLAZE_POWDER).requires(Items.BLUE_ORCHID)
					.requires(Items.COCOA_BEANS, 3)
					.save(pvd);

			unlock(pvd, ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, GLFood.HIGI_DOUGHNUT.item, 1)::unlockedBy, GLFood.HIGI_CHOCOLATE.item.get())
					.requires(YHFood.DOUGHNUT.item).requires(GLFood.HIGI_CHOCOLATE.item)
					.save(pvd);
			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.KOISHI_MOUSSE.item.get(), 1, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(Items.CORNFLOWER)
					.addIngredient(Items.ALLIUM)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(Items.HONEY_BOTTLE)
					.addIngredient(YHItems.CREAM.get())
					.build(pvd);


			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GLItems.STRAW_HAT)::unlockedBy, ModItems.CANVAS.get())
					.pattern(" A ").pattern("ASA")
					.define('A', ModItems.CANVAS.get())
					.define('S', Items.STRING)
					.save(pvd);

			cake(pvd, GLFoodItems.RED_VELVET);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GLFood.FLESH_CHOCOLATE_MOUSSE.item, 4)::unlockedBy, GLFood.FLESH.item.get())
					.pattern(" B ").pattern("FDF").pattern("ECE")
					.define('B', CommonTags.FOODS_MILK)
					.define('C', GLItems.BLOOD_BOTTLE.item)
					.define('D', GLFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.COCOA_BEANS)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GLFood.SCARLET_DEVIL_CAKE.item, 4)::unlockedBy, GLFood.FLESH.item.get())
					.pattern("FBF").pattern("ADA").pattern("ECE")
					.define('A', Items.HONEY_BOTTLE)
					.define('B', CommonTags.FOODS_MILK)
					.define('C', GLItems.BLOOD_BOTTLE.item)
					.define('D', GLFood.FLESH.item)
					.define('E', Items.WHEAT)
					.define('F', Items.PINK_PETALS)
					.save(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.FLESH_DUMPLINGS.item.get(), 2, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(CommonTags.FOODS_DOUGH)
					.addIngredient(GLTagGen.RAW_FLESH)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.CANNED_FLESH.item.get(), 2, 200, 0.1f, GLFoodItems.CAN)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(GLTagGen.RAW_FLESH)
					.addIngredient(CommonTags.FOODS_ONION)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.FAIRY_CANDY.item.get(), 3, 200, 0.1f)
					.setRecipeBookTab(CookingPotRecipeBookTab.MISC)
					.addIngredient(GLItems.FAIRY_ICE_CRYSTAL)
					.addIngredient(Items.SUGAR)
					.addIngredient(Items.HONEY_BOTTLE)
					.build(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GLFoodItems.RAW_FLESH_FEAST, 1)::unlockedBy, GLFood.FLESH.item.get())
					.pattern("FFF").pattern("1F2").pattern("3S4")
					.define('F', GLTagGen.RAW_FLESH)
					.define('S', Items.SKELETON_SKULL)
					.define('1', Tags.Items.CROPS_CARROT)
					.define('2', Items.BROWN_MUSHROOM)
					.define('3', CommonTags.FOODS_ONION)
					.define('4', CommonTags.FOODS_CABBAGE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, GLFoodItems.RED_VELVET.block, 1)::unlockedBy, GLFood.FLESH.item.get())
					.pattern("ABA").pattern("CDC").pattern("EEE")
					.define('A', Items.SUGAR)
					.define('B', Items.MILK_BUCKET)
					.define('C', GLItems.BLOOD_BOTTLE.item)
					.define('D', GLFood.FLESH.item)
					.define('E', Items.WHEAT)
					.save(pvd);


			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.FLESH_STEW.item.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(GLTagGen.RAW_FLESH)
					.addIngredient(GLTagGen.RAW_FLESH)
					.addIngredient(YHTagGen.RAW_EEL)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLFoodItems.FLESH_FEAST.get(), 1, 200, 0.1f, Items.BOWL)
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(GLFoodItems.RAW_FLESH_FEAST)
					.addIngredient(GLTagGen.RAW_FLESH)
					.addIngredient(GLItems.BLOOD_BOTTLE.item)
					.addIngredient(GLItems.BLOOD_BOTTLE.item)
					.addIngredient(YHItems.SOY_SAUCE_BOTTLE.item)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLDish.BLOOD_CURD.block.get(), 1, 200, 0.1f, YHItems.SAUCER.get())
					.setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
					.addIngredient(GLItems.BLOOD_BOTTLE.item)
					.addIngredient(GLItems.BLOOD_BOTTLE.item)
					.addIngredient(Tags.Items.FOODS_VEGETABLE)
					.build(pvd);

			CookingPotRecipeBuilder.cookingPotRecipe(GLFood.SCARLET_TEA.item.get(), 1, 200, 0.1f, Items.GLASS_BOTTLE)
					.setRecipeBookTab(CookingPotRecipeBookTab.DRINKS)
					.unlockedByAnyIngredient(GLItems.BLOOD_BOTTLE.item)
					.addIngredient(YHTagGen.TEA_BLACK)
					.addIngredient(GLItems.BLOOD_BOTTLE.item)
					.build(tea(pvd));

			unlock(pvd, new SimpleFermentationBuilder(GLItems.BLOOD_BOTTLE, GLSake.SCARLET_MIST, 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(Items.ROSE_BUSH).addInput(Items.ROSE_BUSH)
					.addInput(Items.POPPY)
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED))
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.RED))
					.save(pvd, GLSake.SCARLET_MIST.item.getRegisteredName());

			unlock(pvd, new SimpleFermentationBuilder(Tags.Fluids.WATER, GLSake.WIND_PRIESTESSES, 3600)::unlockedBy, ModItems.RICE.get())
					.addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE).addInput(CommonTags.CROPS_RICE)
					.addInput(DanmakuItems.Bullet.CIRCLE.get(DyeColor.LIME))
					.addInput(Items.DANDELION).addInput(YHTagGen.TEA_GREEN).addInput(YHItems.MATCHA)
					.save(pvd, GLSake.WIND_PRIESTESSES.item.getRegisteredName());
		}

		// incense
		{

			unlock(pvd, new SimpleCenserRecipeBuilder(GLMechanics.GLOW.get(), 12000)::unlockedBy, Items.GLOWSTONE_DUST)
					.require(ItemTags.COALS).require(Items.GLOWSTONE_DUST).save(pvd);

			unlock(pvd, new SimpleCenserRecipeBuilder(GLMechanics.HEAL.get(), 6000)::unlockedBy, Items.GHAST_TEAR)
					.require(Items.REDSTONE).require(Items.GLOWSTONE_DUST).require(Items.GHAST_TEAR).save(pvd);

			unlock(pvd, new SimpleCenserRecipeBuilder(GLMechanics.CLEANSE.get(), 12000)::unlockedBy, YHFood.BUTTER.asItem())
					.require(Items.REDSTONE).require(Items.GLOWSTONE_DUST).require(YHFood.BUTTER).save(pvd);
		}

	}


	private static void food(RegistrateRecipeProvider pvd, GLFood raw, GLFood cooked) {
		pvd.food(DataIngredient.items(raw.item.get()), RecipeCategory.FOOD, cooked.item, 0.1f);
	}

	private static void cake(RegistrateRecipeProvider pvd, CakeEntry cake) {
		CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(cake.block.get()),
						Ingredient.of(CommonTags.TOOLS_KNIFE), cake.item.get(), cake.isCake ? 7 : 4)
				.build(pvd);
		if (cake.isCake) {
			unlock(pvd, new ShapelessRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.requires(cake.item.get(), 7)
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		} else {
			unlock(pvd, new ShapedRecipeBuilder(RecipeCategory.FOOD, cake.block.get(), 1)::unlockedBy, cake.item.get())
					.pattern("AA").pattern("AA")
					.define('A', cake.item.get())
					.save(pvd, cake.block.getId().withSuffix("_assemble"));
		}
	}

	private static RecipeOutput tea(RegistrateRecipeProvider pvd) {
		return new BasePotOutput<>(YHBlocks.KETTLE_RS.get(), pvd);
	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}

}
