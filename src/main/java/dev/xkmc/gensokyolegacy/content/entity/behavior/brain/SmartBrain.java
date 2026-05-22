package dev.xkmc.gensokyolegacy.content.entity.behavior.brain;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.*;
import java.util.function.Supplier;

public class SmartBrain<E extends Mob> extends Brain<E> {

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static SmartBrain<?> construct(TaskBoard board, Dynamic<?> dynamic) {
		var ans = new Provider<>(board.memories(), board.getSensors().stream().map(e -> new SensorType(() -> e)).toList()).makeBrain(dynamic);
		ans.setCoreActivities(Set.of(Activity.CORE));
		board.buildBrain(ans);
		return ans;
	}

	private List<Activity> priorityActivities = new ArrayList<>();

	@SuppressWarnings({"unchecked"})
	private SmartBrain(
			Collection<? extends MemoryModuleType<?>> memoryModuleTypes,
			Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes,
			ImmutableList memoryValues,
			Supplier<Codec<Brain<E>>> codec
	) {
		super(memoryModuleTypes, sensorTypes, memoryValues, codec);
	}

	public void setPriorityActivities(List<Activity> list) {
		priorityActivities = list;
	}

	@Override
	public void tick(ServerLevel level, E entity) {
		this.forgetOutdatedMemories();
		this.tickSensors(level, entity);
		updateActivities(level, entity);
		this.startEachNonRunningBehavior(level, entity);
		this.tickEachRunningBehavior(level, entity);
	}

	private void updateActivities(ServerLevel level, E entity) {
		for (var e : priorityActivities) {
			if (isActive(e)) {
				if (activityRequirementsAreMet(e))
					return;
				else lastScheduleUpdate = 0;
			}
			if (activityRequirementsAreMet(e)) {
				setActiveActivityIfPossible(e);
				return;
			}
		}
		updateActivityFromSchedule(level.getDayTime(), level.getGameTime());
	}

	@Override
	public Brain<E> copyWithoutBehaviors() {
		SmartBrain<E> brain = new SmartBrain<>(this.memories.keySet(), this.sensors.keySet(), ImmutableList.of(), this.codec);
		for (Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> entry : this.memories.entrySet()) {
			MemoryModuleType<?> memorymoduletype = entry.getKey();
			if (entry.getValue().isPresent()) {
				brain.memories.put(memorymoduletype, entry.getValue());
			}
		}
		return brain;
	}

	public static final class Provider<E extends Mob> {
		private final Collection<? extends MemoryModuleType<?>> memoryTypes;
		private final Collection<? extends SensorType<? extends Sensor<? super E>>> sensorTypes;
		private final Codec<Brain<E>> codec;

		@SuppressWarnings({"rawtypes", "unchecked"})
		public Provider(Collection memoryTypes, Collection sensorTypes) {
			this.memoryTypes = memoryTypes;
			this.sensorTypes = sensorTypes;
			this.codec = Brain.codec(memoryTypes, sensorTypes);
		}

		public SmartBrain<E> makeBrain(Dynamic<?> ops) {
			return this.codec
					.parse(ops)
					.resultOrPartial(Brain.LOGGER::error)
					.map(e -> new SmartBrain<>(this.memoryTypes, this.sensorTypes, ImmutableList.copyOf(e.memories().toList()), () -> this.codec))
					.orElseGet(() -> new SmartBrain<>(this.memoryTypes, this.sensorTypes, ImmutableList.of(), () -> this.codec));
		}

	}

}
