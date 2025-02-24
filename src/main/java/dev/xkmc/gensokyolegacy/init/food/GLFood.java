package dev.xkmc.gensokyolegacy.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import dev.xkmc.youkaishomecoming.compat.diet.DietTagGen;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.List;
import java.util.Locale;

public enum GLFood implements ItemLike {

	KOISHI_MOUSSE(FoodType.SIMPLE, 6, 0.6f, new EffectEntry(GLEffects.UNCONSCIOUS, 400, 0, 1), DietTagGen.SUGARS.tag),
	HIGI_CHOCOLATE(FoodType.SIMPLE, 3, 0.8f, List.of(
			new EffectEntry(GLEffects.HIGI, 1200, 0, 1)
	), DietTagGen.SUGARS.tag),
	HIGI_DOUGHNUT(FoodType.SIMPLE, 8, 0.8f, List.of(
			new EffectEntry(GLEffects.HIGI, 1200, 0, 1),
			new EffectEntry(ModEffects.NOURISHMENT, 600, 0, 1)
	), DietTagGen.GRAINS.tag, DietTagGen.SUGARS.tag),
	FAIRY_CANDY(FoodType.FAST, 2, 0.6f, List.of(
			new EffectEntry(GLEffects.FAIRY, 1, 0, 1)
	), DietTagGen.SUGARS.tag),

	FLESH(GLFoodType.FLESH, 2, 0.3f, GLTagGen.RAW_FLESH, GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	COOKED_FLESH(GLFoodType.FLESH, 5, 0.8f, GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),

	SCARLET_TEA(FoodType.BOTTLE, 0, 0, List.of(
			new EffectEntry(YHEffects.TEA, 1200, 0, 1),
			new EffectEntry(YHEffects.THICK, 600, 0, 1),
			new EffectEntry(GLEffects.VAMPIRE, 1, 0, 1)
	), GLTagGen.FLESH_FOOD),
	FLESH_DUMPLINGS(GLFoodType.FLESH, 2, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 2400, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 2400, 0, 1)
	), DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	FLESH_ROLL(GLFoodType.FLESH, 3, 0.8f,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag, DietTagGen.GRAINS.tag),
	CANNED_FLESH(GLFoodType.CAN_FLESH, 4, 0.8f, GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_STEW(GLFoodType.BOWL_FLESH, 7, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag, DietTagGen.VEGETABLES.tag),
	BOWL_OF_FLESH_FEAST(GLFoodType.BOWL_FLESH, 5, 0.8f, List.of(
			new EffectEntry(ModEffects.NOURISHMENT, 6000, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 6000, 0, 1)
	), GLTagGen.APPARENT_FLESH_FOOD, DietTagGen.PROTEINS.tag),
	FLESH_CHOCOLATE_MOUSSE(GLFoodType.FLESH_FAST, 3, 0.5f, DietTagGen.PROTEINS.tag, DietTagGen.SUGARS.tag),
	SCARLET_DEVIL_CAKE(GLFoodType.FLESH_FAST, 4, 0.5f, DietTagGen.PROTEINS.tag, DietTagGen.SUGARS.tag),
	;


	public final ItemEntry<Item> item;

	private final FoodType type;

	@SafeVarargs
	GLFood(FoodType type, int nutrition, float sat, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.type = type;
		String name = name().toLowerCase(Locale.ROOT);
		String id = "food/simple/";
		if (type == FoodType.BOTTLE) id = "food/bottle/";
		if (type instanceof GLFoodType.FleshFoodType) id = "food/flesh/";
		item = type.build(GensokyoLegacy.REGISTRATE, id, name, nutrition, sat, tags, effs);
	}

	@SafeVarargs
	GLFood(FoodType type, int nutrition, float sat, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(), tags);
	}

	@SafeVarargs
	GLFood(FoodType type, int nutrition, float sat, EffectEntry eff, TagKey<Item>... tags) {
		this(type, nutrition, sat, List.of(eff), tags);
	}

	public static void register() {

	}

	@Override
	public Item asItem() {
		return item.asItem();
	}

}
