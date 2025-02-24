package dev.xkmc.gensokyolegacy.content.food.flesh;

import dev.xkmc.gensokyolegacy.init.food.GLFoodItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CannedFleshFoodItem extends FleshFoodItem {

	public CannedFleshFoodItem(Properties props) {
		super(props);
	}

	@Override
	protected void modifyFood(ItemStack stack, @Nullable LivingEntity entity, FoodProperties.Builder builder) {
		builder.usingConvertsTo(GLFoodItems.CAN);
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
		return GLFoodItems.CAN.asStack();
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return true;
	}
}
