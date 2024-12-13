package dev.xkmc.gensokyolegacy.content.entity.behavior.task;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TaskBoard {

	public enum Type {
		FIRST, RANDOM
	}

	public record Entry(int priority, ExtendedBehaviour<?> behavior, Set<Activity> activities) {

	}

	private final List<Entry> first = new ArrayList<>();
	private final List<Entry> random = new ArrayList<>();
	private final List<ExtendedSensor<? extends SmartYoukaiEntity>> sensors = new ArrayList<>();

	public void addFirst(int priority, ExtendedBehaviour<?> behavior, Activity... activities) {
		first.add(new Entry(priority, behavior, Set.of(activities)));
	}

	public void addRandom(ExtendedBehaviour<?> behavior, Activity... activities) {
		random.add(new Entry(0, behavior, Set.of(activities)));
	}

	public void addSensor(ExtendedSensor<? extends SmartYoukaiEntity> sensor) {
		this.sensors.add(sensor);
	}

	public void build() {
		first.sort(Comparator.comparingInt(e -> e.priority));
	}

	public List<ExtendedSensor<? extends SmartYoukaiEntity>> getSensors() {
		return sensors;
	}

	@SuppressWarnings("unchecked")
	public Behavior<? super SmartYoukaiEntity>[] fetch(Activity activity) {
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
