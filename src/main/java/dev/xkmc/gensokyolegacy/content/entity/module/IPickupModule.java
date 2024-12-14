package dev.xkmc.gensokyolegacy.content.entity.module;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface IPickupModule {

	boolean canPickup(ItemStack stack);

	void onPickup(ItemEntity entity);

}
