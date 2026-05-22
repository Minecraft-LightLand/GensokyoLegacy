package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.function.Function;

public abstract class DynamicSensor<E extends Mob> extends Sensor<E> {

	private Function<E, Integer> tickRate = e -> 20;

	public DynamicSensor<E> setScanRate(Function<E, Integer> function) {
		this.tickRate = function;
		return this;
	}

	public void tick(ServerLevel level, E entity) {
		if (--timeToTick <= 0L) {
			timeToTick = tickRate.apply(entity);
			doTick(level, entity);
		}
	}

}
