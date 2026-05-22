package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public class AbstractNearbyEntitySensor<T extends Entity, E extends Mob> extends DynamicSensor<E> {

	protected double xz, y;

	protected BiPredicate<T, E> predicate;
	protected Class<T> cls;
	protected MemoryModuleType<List<T>> memory;

	public AbstractNearbyEntitySensor(MemoryModuleType<List<T>> memory, Class<T> cls, BiPredicate<T, E> predicate) {
		this.memory = memory;
		this.cls = cls;
		this.predicate = predicate;
	}

	public AbstractNearbyEntitySensor<T, E> setRadius(double xz, double y) {
		this.xz = xz;
		this.y = y;
		return this;
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return Set.of(memory);
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		var list = level.getEntities(EntityTypeTest.forClass(cls),
				entity.getBoundingBox().inflate(xz, y, xz),
				e -> predicate.test(e, entity));
		setMemory(level, entity, list);
	}

	protected void setMemory(ServerLevel level, E entity, List<T> list) {
		BrainUtils.setMemory(entity, memory, list);
	}

}


