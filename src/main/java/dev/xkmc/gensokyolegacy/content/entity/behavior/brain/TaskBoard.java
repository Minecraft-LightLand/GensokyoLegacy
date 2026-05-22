package dev.xkmc.gensokyolegacy.content.entity.behavior.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Registration util for Activity and Behavior.
 * There are 2 kinds of activities: prioritized activities and scheduled activities.
 * Start memory requirement of a prioritized activity will also be
 * the start memory-absence requirement of all less prioritized activities,
 * to ensure exclusiveness of activity start conditions and enforce priority.
 * There are 3 kinds of behaviors: always, exclusive, and random.
 * All always-type, 1 exclusive-type, and 1 random-type behaviors may execute simultaneously.
 */
public class TaskBoard {

	private record ActivityEntry(Activity activity, @Nullable MemoryModuleType<?> memory, int priority) {
	}

	private record BehaviorEntry(int priority, BehaviorControl<?> behavior, Set<Activity> activities) {
	}

	private final Map<Activity, ActivityEntry> activities = new LinkedHashMap<>();
	private final List<ActivityEntry> priorities = new ArrayList<>();
	private final List<Activity> priority = new ArrayList<>();
	private final List<BehaviorEntry> always = new ArrayList<>();
	private final List<BehaviorEntry> first = new ArrayList<>();
	private final List<BehaviorEntry> random = new ArrayList<>();
	private final List<Sensor<?>> sensors = new ArrayList<>();
	private final Map<Class<?>, BehaviorEntry> map = new HashMap<>();
	private final Set<MemoryModuleType<?>> memories = new LinkedHashSet<>();

	private Schedule schedule = null;

	private void addMemories(BehaviorControl<?> ctrl) {
		if (ctrl instanceof Behavior<?> beh) {
			memories.addAll(beh.entryCondition.keySet());
		}
	}

	public Set<MemoryModuleType<?>> memories() {
		return memories;
	}

	/**
	 * Add an always-executing behavior to some activities
	 */
	public void addAlways(BehaviorControl<?> behavior, Activity... activities) {
		var entry = new BehaviorEntry(0, behavior, new LinkedHashSet<>(Set.of(activities)));
		always.add(entry);
		map.put(behavior.getClass(), entry);
		addMemories(behavior);
	}

	/**
	 * Add an exclusive behavior to some activities.
	 * Only 1 exclusive behavior can run at a time.
	 * Behavior with the smallest priority number will run.
	 */
	public void addExclusive(int priority, BehaviorControl<?> behavior, Activity... activities) {
		var entry = new BehaviorEntry(priority, behavior, new LinkedHashSet<>(Set.of(activities)));
		first.add(entry);
		map.put(behavior.getClass(), entry);
		addMemories(behavior);
	}

	/**
	 * Add a randomly-executing behavior to some activities.
	 * Randomly-executing behavior can run in parallel with exclusive behaviors,
	 * but only 1 randomly executing behavior will execute at a time.
	 */
	public void addRandom(BehaviorControl<?> behavior, Activity... activities) {
		var entry = new BehaviorEntry(0, behavior, new LinkedHashSet<>(Set.of(activities)));
		random.add(entry);
		map.put(behavior.getClass(), entry);
		addMemories(behavior);
	}

	/**
	 * Add an existing behavior to an extra activity
	 */
	public void addBehaviorActivity(Class<?> cls, Activity activity) {
		map.get(cls).activities.add(activity);
	}

	/**
	 * Add a sensor
	 */
	public void addSensor(Sensor<?> sensor) {
		this.sensors.add(sensor);
	}

	/**
	 * Register an activity as a scheduled activity.
	 * If there is an associated memory type,
	 * the activity will not be executed without presence of that memory.
	 */
	public void addScheduledActivity(Activity activity, @Nullable MemoryModuleType<?> test) {
		activities.put(activity, new ActivityEntry(activity, test, Integer.MAX_VALUE));
	}

	/**
	 * Register an activity as a prioritized activity.
	 * Activity with smaller priority number will be prioritized.
	 * If there is an associated memory type,
	 * the activity will not be executed without presence of that memory.
	 * Memory type requirement of a prioritized activity will also be added to the list of
	 * must-be-absent memory requirements of all other less prioritized activities.
	 */
	public void addPrioritizedActivity(Activity activity, @Nullable MemoryModuleType<?> test, int priority) {
		var e = new ActivityEntry(activity, test, priority);
		activities.put(activity, e);
		priorities.add(e);
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	/**
	 * Finish constructing the task board and sort everything.
	 */
	public void build() {
		addPrioritizedActivity(Activity.FIGHT, MemoryModuleType.ATTACK_TARGET, 0);
		priorities.sort(Comparator.comparingInt(e -> e.priority));
		first.sort(Comparator.comparingInt(e -> e.priority));
		for (var e : priorities) {
			priority.add(e.activity);
		}
	}

	public List<Sensor<?>> getSensors() {
		return sensors;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void buildBrain(SmartBrain<?> brain) {
		var list = new ArrayList<Activity>();
		list.add(Activity.CORE);
		for (var e : priorities) {
			list.add(e.activity);
		}
		for (var e : activities.entrySet()) {
			if (e.getValue().priority == Integer.MAX_VALUE) {
				list.add(e.getKey());
			}
		}
		int index = 0;
		Set<MemoryModuleType<?>> previous = new LinkedHashSet<>();
		for (var act : list) {
			var behaviors = fetch(act);
			var copy = new ArrayList();
			for (var e : behaviors) {
				copy.add(Pair.of(index++, e));
			}
			Set<Pair<MemoryModuleType<?>, MemoryStatus>> current = new LinkedHashSet<>();
			for (var e : previous) {
				current.add(Pair.of(e, MemoryStatus.VALUE_ABSENT));
			}
			var entry = activities.get(act);
			if (entry != null) {
				var mem = entry.memory();
				if (mem != null) {
					if (entry.priority < Integer.MAX_VALUE) previous.add(mem);
					current.add(Pair.of(mem, MemoryStatus.VALUE_PRESENT));
				}
			}
			brain.setPriorityActivities(priority);
			brain.addActivityWithConditions(act, ImmutableList.copyOf(copy), current);
		}
		if (schedule != null)
			brain.setSchedule(schedule);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private List<BehaviorControl<?>> fetch(Activity activity) {
		List<Pair<BehaviorControl<?>, Integer>> subFirst = new ArrayList<>();
		for (var e : first) {
			if (e.activities.contains(activity))
				subFirst.add(Pair.of(e.behavior, 100));
		}
		List<Pair<BehaviorControl<?>, Integer>> subRandom = new ArrayList<>();
		for (var e : random) {
			if (e.activities.contains(activity))
				subRandom.add(Pair.of(e.behavior, 100));
		}
		List<BehaviorControl<?>> behaviors = new ArrayList<>();
		for (var e : always) {
			if (e.activities.contains(activity))
				behaviors.add(e.behavior);
		}
		if (subFirst.size() > 1)
			behaviors.add(new GateBehavior(ImmutableMap.of(), ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, subFirst));
		else if (subFirst.size() == 1)
			behaviors.add(subFirst.getFirst().getFirst());
		if (subRandom.size() > 1)
			behaviors.add(new GateBehavior(ImmutableMap.of(), ImmutableSet.of(), GateBehavior.OrderPolicy.SHUFFLED, GateBehavior.RunningPolicy.RUN_ONE, subRandom));
		else if (subRandom.size() == 1)
			behaviors.add(subRandom.getFirst().getFirst());
		return behaviors;
	}

}
