package dev.xkmc.gensokyolegacy.content.entity.youkai;

import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiUpdateHomeSensor;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiAttackTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiFetchTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiSearchTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiUpdateTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.*;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.home.*;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.GroupBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.object.MemoryTest;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@SerialClass
public class SmartYoukaiEntity extends YoukaiEntity implements SmartBrainOwner<SmartYoukaiEntity> {

	private TaskBoard board;

	public SmartYoukaiEntity(EntityType<? extends YoukaiEntity> pEntityType, Level pLevel, int maxSize) {
		super(pEntityType, pLevel, maxSize);
	}

	public boolean hasPlayerNearby() {
		return getBrain().getMemory(MemoryModuleType.NEAREST_PLAYERS)
				.map(List::size).orElse(0) > 0;
	}

	public Activity getActivity() {
		return getBrain().getActiveNonCoreActivity().orElse(Activity.IDLE);
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		tickBrain(this);
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	protected void constructTaskBoard(TaskBoard board) {
		board.addFirst(100, new YoukaiFetchTargetTask<>(), GLBrains.AT_HOME.get(), Activity.REST);
		board.addFirst(101, new YoukaiSearchTargetTask<>(), Activity.IDLE, Activity.PLAY);
		board.addFirst(150, new YoukaiVanishTask(), Activity.IDLE, Activity.PLAY);
		board.addFirst(200, new YoukaiSleepTask(), Activity.REST);
		board.addFirst(300, new YoukaiGoHomeTask<>(), Activity.IDLE, GLBrains.AT_HOME.get());
		board.addFirst(1100, new SetPlayerLookTarget<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
		board.addFirst(1200, new SetRandomLookTarget<>(), Activity.IDLE, Activity.PLAY);

		board.addRandom(new SetRandomWalkTarget<>().speedModifier(0.8f), Activity.IDLE, Activity.PLAY);
		board.addRandom(new YoukaiStayInRoomTask<>().speedModifier(0.8f), GLBrains.AT_HOME.get());
		board.addRandom(new YoukaiStayNearHouseTask<>().speedModifier(0.8f)
				.cooldownFor(e -> e.getRandom().nextInt(200, 400)), Activity.IDLE);
		board.addRandom(new YoukaiSitTask<>().speedModifier(0.8f)
				.cooldownFor(e -> e.getRandom().nextInt(200, 400))
				.runFor(e -> e.getRandom().nextInt(100, 200)), GLBrains.AT_HOME.get());
		board.addRandom(new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)),
				Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());

		board.addSensor(new NearbyPlayersSensor<SmartYoukaiEntity>().setRadius(32).setScanRate(e -> 5));
		board.addSensor(new NearbyLivingEntitySensor<SmartYoukaiEntity>().setRadius(32)
				.setScanRate(self -> self.isAggressive() || self.hasPlayerNearby() ? 10 : 20));
		board.addSensor(new YoukaiUpdateHomeSensor<SmartYoukaiEntity>().setScanRate(e -> 80));

		var idle = MemoryTest.builder(1).noMemory(MemoryModuleType.ATTACK_TARGET);
		var home = MemoryTest.builder(2).noMemory(MemoryModuleType.ATTACK_TARGET).hasMemory(MemoryModuleType.HOME);
		board.addScheduledActivity(Activity.REST, home);
		board.addScheduledActivity(GLBrains.AT_HOME.get(), home);
		board.addScheduledActivity(Activity.PLAY, idle);
		board.addScheduledActivity(Activity.IDLE, idle);
	}

	private void checkBoard() {
		if (board == null) {
			board = new TaskBoard();
			constructTaskBoard(board);
			board.build();
		}
	}

	@Override
	public final List<ExtendedSensor<? extends SmartYoukaiEntity>> getSensors() {
		checkBoard();
		return board.getSensors();
	}

	@Override
	public BrainActivityGroup<? extends SmartYoukaiEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(
				new LookAtTarget<>()
						.stopIf(LivingEntity::isSleeping)
						.runFor(entity -> entity.getRandom().nextIntBetweenInclusive(40, 300)),
				new YoukaiMoveTask<>(),
				new YoukaiSwimTask(0.8f),
				new YoukaiSmartDoorTask<>()
		);
	}

	@Override
	public BrainActivityGroup<? extends SmartYoukaiEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new YoukaiUpdateTargetTask<>(),
				new YoukaiAttackTask<>(16),
				new StrafeTarget<>()
		);
	}

	@Override
	public Map<Activity, BrainActivityGroup<? extends SmartYoukaiEntity>> getAdditionalTasks() {
		checkBoard();
		return board.buildActivityMap();
	}

	@Override
	public List<Activity> getActivityPriorities() {
		checkBoard();
		return board.getActivityPriorities();
	}

	@Override
	public @Nullable SmartBrainSchedule getSchedule() {
		return new DefaultedSmartBrainSchedule()
				.activityAt(10, GLBrains.AT_HOME.get())
				.activityAt(2000, Activity.IDLE)
				.activityAt(4000, Activity.PLAY)
				.activityAt(8000, Activity.IDLE)
				.activityAt(10000, GLBrains.AT_HOME.get())
				.activityAt(12000, Activity.REST);
	}

	public String getBrainDebugInfo() {
		var behaviors = getBrain().getRunningBehaviors();
		StringBuilder ans = new StringBuilder();
		for (var e : behaviors) {
			if (e instanceof GroupBehaviour g) {
				var arr = g.debugString().split(" ");
				if (arr.length > 1)
					ans.append("\n-").append(arr[1]);
			} else ans.append("\n-").append(e.debugString());
		}
		return getBrain().getActiveNonCoreActivity().map(Activity::getName).orElse("") + ans;
	}

}
