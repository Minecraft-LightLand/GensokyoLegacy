package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class YoukaiFindPreySensor<T extends SmartYoukaiEntity> extends ExtendedSensor<T> {

	private final Predicate<Activity> time;
	private final BiPredicate<T, LivingEntity> pred;

	public YoukaiFindPreySensor() {
		this(e -> false, (s, e) -> false);
	}

	public YoukaiFindPreySensor(Predicate<Activity> time, BiPredicate<T, LivingEntity> pred) {
		this.time = time;
		this.pred = pred;
	}

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return List.of(GLBrains.MEM_PREY.get());
	}

	@Override
	public SensorType<? extends ExtendedSensor<?>> type() {
		return GLBrains.SN_HUNT.get();
	}

	@Override
	protected void doTick(ServerLevel level, T entity) {
		if (!time.test(entity.getActivity())) return;
		var list = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
		if (list == null) return;
		var opt = list.findClosest(e -> pred.test(entity, e));
		if (opt.isEmpty()) return;
		BrainUtils.setMemory(entity, GLBrains.MEM_PREY.get(), opt.get());
	}

}
