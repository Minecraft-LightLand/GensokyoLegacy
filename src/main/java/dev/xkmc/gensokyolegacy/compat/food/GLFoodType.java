package dev.xkmc.gensokyolegacy.compat.food;

import dev.xkmc.gensokyolegacy.compat.food.flesh.FleshFoodItem;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import dev.xkmc.youkaishomecoming.init.registrate.YHItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;
import java.util.function.Supplier;

public class GLFoodType {

	public static final FoodType FLESH = new FleshFoodType(FleshFoodItem::new, false, false, null, GLTagGen.FLESH_FOOD);
	public static final FoodType FLESH_FAST = new FleshFoodType(FleshFoodItem::new, true, false, null, GLTagGen.FLESH_FOOD);
	public static final FoodType BOWL_FLESH = new FleshFoodType(FleshFoodItem::new, false, false, () -> Items.BOWL, GLTagGen.FLESH_FOOD);
	public static final FoodType CAN_FLESH = new FleshFoodType(FleshFoodItem::new, true, false, YHItems.CAN, GLTagGen.FLESH_FOOD);

	public static class FleshFoodType extends FoodType {

		@SafeVarargs
		public FleshFoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable Supplier<Item> container, EffectEntry[] effs, TagKey<Item>... tags) {
			super(factory, fast, alwaysEat, container, effs, tags);
		}

		@SafeVarargs
		public FleshFoodType(Function<Item.Properties, Item> factory, boolean fast, boolean alwaysEat, @Nullable Supplier<Item> container, TagKey<Item>... tags) {
			super(factory, fast, alwaysEat, container, tags);
		}

		public String makeLang(String id) {
			String name = YHItems.toEnglishName(id.toLowerCase(Locale.ROOT));
			name = name.replaceFirst("Flesh", "%1\\$s");
			return YHItems.toEnglishName(name);
		}

	}

}
