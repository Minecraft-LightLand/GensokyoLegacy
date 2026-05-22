package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;

public class NearbyItemsSensor<E extends Mob> extends AbstractNearbyEntitySensor<ItemEntity, E> {

	public NearbyItemsSensor() {
		super(GLBrains.MEM_ITEMS.get(), ItemEntity.class,
				(item, entity) -> entity.wantsToPickUp(item.getItem()) && entity.hasLineOfSight(item));
	}

}


