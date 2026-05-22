package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class NearbyLivingEntitySensor<E extends Mob> extends AbstractNearbyEntitySensor<LivingEntity, E> {

	public NearbyLivingEntitySensor() {
		super(MemoryModuleType.NEAREST_LIVING_ENTITIES, LivingEntity.class,
				(target, entity) -> target != entity && target.isAlive());
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return Set.of(
				MemoryModuleType.NEAREST_LIVING_ENTITIES,
				MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES
		);
	}

	@Override
	protected void setMemory(ServerLevel level, E entity, List<LivingEntity> list) {
		list.sort(Comparator.comparingDouble(entity::distanceToSqr));
		BrainUtils.setMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
		BrainUtils.setMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(entity, list));
	}
}
