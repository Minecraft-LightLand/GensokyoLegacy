package dev.xkmc.gensokyolegacy.content.mechanics.incense.core;

import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IncenseItem extends Item {

	public IncenseItem(Properties properties) {
		super(properties);
	}

	public Incense get() {
		var ans = GLMechanics.INCENSE.reg().get(builtInRegistryHolder().unwrapKey().orElseThrow().location());
		assert ans != null;
		return ans;
	}

	@Override
	protected String getOrCreateDescriptionId() {
		return Util.makeDescriptionId(GLMechanics.INCENSE.key().location().getPath(), BuiltInRegistries.ITEM.getKey(this));
	}

	public int getIncenseDuration(Level level, ItemStack stack) {
		return GLItems.INCENSE_DUR.getOrDefault(stack, Integer.MAX_VALUE);
	}

	public int getIncenseRadius(Level level, ItemStack stack) {
		return GLItems.INCENSE_RAD.getOrDefault(stack, get().getRadius(level));
	}
}
