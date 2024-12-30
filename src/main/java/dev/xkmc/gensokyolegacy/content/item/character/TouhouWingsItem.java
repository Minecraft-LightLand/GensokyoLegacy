package dev.xkmc.gensokyolegacy.content.item.character;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TouhouWingsItem extends Item {

	public TouhouWingsItem(Properties pProperties) {
		super(pProperties.stacksTo(1));
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.CHEST;
	}

}
