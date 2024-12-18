package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.entity.behavior.move.CompoundPath;
import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiFindPreySensor;
import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiUpdateHomeSensor;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Optional;

public class GLBrains {

	private static final SR<SensorType<?>> SENSORS = SR.of(GensokyoLegacy.REG, BuiltInRegistries.SENSOR_TYPE);
	private static final SR<MemoryModuleType<?>> MEMORIES = SR.of(GensokyoLegacy.REG, BuiltInRegistries.MEMORY_MODULE_TYPE);
	private static final SR<Activity> ACTIVITIES = SR.of(GensokyoLegacy.REG, BuiltInRegistries.ACTIVITY);

	public static final Val<SensorType<YoukaiUpdateHomeSensor<?>>> SN_HOME = SENSORS.reg("home", () -> new SensorType<>(YoukaiUpdateHomeSensor::new));
	public static final Val<SensorType<YoukaiFindPreySensor<?>>> SN_HUNT = SENSORS.reg("hunt", () -> new SensorType<>(YoukaiFindPreySensor::new));

	public static final Val<MemoryModuleType<CompoundPath>> MEM_PATH = MEMORIES.reg("path", () -> new MemoryModuleType<>(Optional.empty()));
	public static final Val<MemoryModuleType<LivingEntity>> MEM_PREY = MEMORIES.reg("prey", () -> new MemoryModuleType<>(Optional.empty()));
	public static final Val<MemoryModuleType<Unit>> MEM_DOWN = MEMORIES.reg("down", () -> new MemoryModuleType<>(Optional.of(Unit.CODEC)));
	public static final Val<MemoryModuleType<Player>> MEM_TALK = MEMORIES.reg("talk", () -> new MemoryModuleType<>(Optional.empty()));

	public static final Val<Activity> AT_HOME = ACTIVITIES.reg("at_home", () -> new Activity("at_home"));
	public static final Val<Activity> HUNT = ACTIVITIES.reg("hunt", () -> new Activity("hunt"));
	public static final Val<Activity> DOWN = ACTIVITIES.reg("down", () -> new Activity("down"));
	public static final Val<Activity> TALK = ACTIVITIES.reg("talk", () -> new Activity("talk"));

	public static void register() {
	}

}
