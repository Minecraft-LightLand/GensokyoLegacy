package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

@SerialClass
public class CountPickupModule extends AbstractYoukaiModule implements IPickupModule {

	private static final ResourceLocation ID = GensokyoLegacy.loc("count_pickup");

	private final Predicate<ItemStack> pred;

	@SerialField
	private int count = 0;

	public CountPickupModule(YoukaiEntity self, Predicate<ItemStack> pred) {
		super(ID, self);
		this.pred = pred;
	}

	@Override
	public boolean canPickup(ItemStack stack) {
		return pred.test(stack);
	}

	@Override
	public void onPickup(ItemEntity entity) {
		count += entity.getItem().getCount();
		entity.discard();
	}

	public int getCount() {
		return count;
	}

	public void consume(int n) {
		count -= n;
	}

}
