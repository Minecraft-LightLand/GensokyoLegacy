package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class IngredientHistory {

	public final List<ItemStack> items = new ArrayList<>();

	public void add(ItemStack stack) {
		items.add(stack);
	}

	public void clear() {
		items.clear();
	}

}
