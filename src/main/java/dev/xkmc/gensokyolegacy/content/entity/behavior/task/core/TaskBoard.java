package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.*;

public class TaskBoard {

	private record ActivityEntry(Activity activity, MemoryTest test, int priority) {
	}

	private record BehaviorEntry(int priority, ExtendedBehaviour<?> behavior, Set<Activity> activities) {
	}

	private final Map<Activity, ActivityEntry> activities = new LinkedHashMap<>();
	private final List<ActivityEntry> priorities = new ArrayList<>();
	private final List<Activity> priority = new ArrayList<>();
	private final List<BehaviorEntry> first = new ArrayList<>();
	private final List<BehaviorEntry> random = new ArrayList<>();
	private final List<ExtendedSensor<? extends SmartYoukaiEntity>> sensors = new ArrayList<>();
	private final Map<Class<?>, BehaviorEntry> map = new HashMap<>();

	public void addFirst(int priority, ExtendedBehaviour<?> behavior, Activity... activities) {
		var entry = new BehaviorEntry(priority, behavior, new LinkedHashSet<>(Set.of(activities)));
		first.add(entry);
		map.put(behavior.getClass(), entry);
	}

	public void addRandom(ExtendedBehaviour<?> behavior, Activity... activities) {
		var entry = new BehaviorEntry(0, behavior, new LinkedHashSet<>(Set.of(activities)));
		random.add(entry);
		map.put(behavior.getClass(), entry);
	}

	public void addBehaviorActivity(Class<?> cls, Activity activity) {
		map.get(cls).activities.add(activity);
	}

	public void addSensor(ExtendedSensor<? extends SmartYoukaiEntity> sensor) {
		this.sensors.add(sensor);
	}

	public void addScheduledActivity(Activity activity, MemoryTest test) {
		activities.put(activity, new ActivityEntry(activity, test, 0));
	}

	public void addPrioritizedActivity(Activity activity, MemoryTest test, int priority) {
		var e = new ActivityEntry(activity, test, priority);
		activities.put(activity, e);
		priorities.add(e);
	}

	public void build() {
		priorities.add(new ActivityEntry(Activity.FIGHT, MemoryTest.builder(0), 0));
		priorities.sort(Comparator.comparingInt(e -> e.priority));
		first.sort(Comparator.comparingInt(e -> e.priority));
		for (var e : priorities) {
			priority.add(e.activity);
		}
	}

	public List<ExtendedSensor<? extends SmartYoukaiEntity>> getSensors() {
		return sensors;
	}

	public Map<Activity, BrainActivityGroup<? extends SmartYoukaiEntity>> buildActivityMap() {
		Map<Activity, BrainActivityGroup<? extends SmartYoukaiEntity>> ans = new LinkedHashMap<>();
		for (var ent : activities.entrySet()) {
			ans.put(ent.getKey(), buildActivityGroup(ent.getValue()));
		}
		return ans;
	}

	public List<Activity> getActivityPriorities() {
		return priority;
	}

	private BrainActivityGroup<SmartYoukaiEntity> buildActivityGroup(ActivityEntry act) {
		var entry = new BrainActivityGroup<SmartYoukaiEntity>(act.activity)
				.priority(10).behaviours(fetch(act.activity));
		for (var e : act.test) {
			entry.onlyStartWithMemoryStatus(e.getFirst(), e.getSecond());
		}
		return entry;
	}

	@SuppressWarnings("unchecked")
	private Behavior<? super SmartYoukaiEntity>[] fetch(Activity activity) {
		List<ExtendedBehaviour<?>> subFirst = new ArrayList<>();
		for (var e : first) {
			if (e.activities.contains(activity))
				subFirst.add(e.behavior);
		}
		List<ExtendedBehaviour<?>> subRandom = new ArrayList<>();
		for (var e : random) {
			if (e.activities.contains(activity))
				subRandom.add(e.behavior);
		}
		return new Behavior[]{
				new FirstApplicableBehaviour<>(subFirst.toArray(ExtendedBehaviour[]::new)),
				new OneRandomBehaviour<>(subRandom.toArray(ExtendedBehaviour[]::new))
		};
	}

}
