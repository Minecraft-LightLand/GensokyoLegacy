package dev.xkmc.gensokyolegacy.compat.food;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import dev.xkmc.youkaishomecoming.content.item.fluid.*;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.FoodType;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.Locale;

public enum GLSake implements IYHSake {
	SCARLET_MIST(FoodType.BOTTLE, 0xFFEA6B88, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 1, 1),
			new EffectEntry(GLEffects.VAMPIRE, 1, 1, 1)
	), GLTagGen.FLESH_FOOD),
	WIND_PRIESTESSES(FoodType.BOTTLE, 0xFF79E1CA, List.of(
			new EffectEntry(YHEffects.DRUNK, 1200, 0, 1),
			new EffectEntry(GLEffects.NATIVE, 600, 0, 1))),
	;

	public final int color;

	public final FluidEntry<SakeFluid> fluid;
	public final ItemEntry<Item> item;

	@SafeVarargs
	GLSake(FoodType type, int color, List<EffectEntry> effs, TagKey<Item>... tags) {
		this.color = color;
		String name = name().toLowerCase(Locale.ROOT);
		fluid = BottledFluid.water(name, (p, s, f) -> new SakeFluidType(p, s, f, this), p -> new SakeFluid(p, this))
				.defaultLang().register();
		item = type.build(p -> new SakeBottleItem(fluid, p), "sake/", name, 0, 0, tags, effs);
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public ItemEntry<?> item() {
		return item;
	}

	@Override
	public FluidEntry<? extends SakeFluid> fluid() {
		return fluid;
	}

	@SuppressWarnings("deprecation")
	public Item getContainer() {
		Item ans = item.get().getCraftingRemainingItem();
		if (ans == Items.BAMBOO) {
			ans = BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace("bamboo"));
		}
		if (ans == null) return Items.AIR;
		return ans;
	}

	@Override
	public ItemStack asStack(int count) {
		return item.asStack(count);
	}

	public static void register() {

	}

}
