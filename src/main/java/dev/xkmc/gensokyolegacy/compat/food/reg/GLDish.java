package dev.xkmc.gensokyolegacy.compat.food.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.content.block.food.FoodSaucerBlock;
import dev.xkmc.youkaishomecoming.init.food.EffectEntry;
import dev.xkmc.youkaishomecoming.init.food.IYHDish;
import dev.xkmc.youkaishomecoming.init.food.Saucer;
import net.minecraft.world.food.FoodProperties;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum GLDish implements IYHDish {
	BLOOD_CURD(Saucer.SAUCER_3, 8, 0.8f, 4,
			new EffectEntry(ModEffects.NOURISHMENT, 3600, 0, 1),
			new EffectEntry(ModEffects.COMFORT, 3600, 0, 1)),
	;

	public final Saucer base;
	public final int height;

	public final BlockEntry<FoodSaucerBlock> block;

	GLDish(Saucer base, int nutrition, float sat, int height, EffectEntry... effs) {
		this.base = base;
		this.height = height;
		var builder = new FoodProperties.Builder()
				.nutrition(nutrition).saturationModifier(sat);
		for (var e : effs) {
			builder.effect(e::getEffect, e.chance());
		}
		var food = builder.build();
		block = buildBlock(GensokyoLegacy.REGISTRATE, food);
	}

	@Override
	public Saucer base() {
		return base;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public BlockEntry<FoodSaucerBlock> block() {
		return block;
	}

	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static void register() {
	}

}