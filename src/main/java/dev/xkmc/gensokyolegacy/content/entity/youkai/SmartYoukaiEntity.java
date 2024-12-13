package dev.xkmc.gensokyolegacy.content.entity.youkai;

import dev.xkmc.gensokyolegacy.content.entity.behavior.sensor.YoukaiUpdateHomeSensor;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiAttackTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiFetchTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiSearchTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiUpdateTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.YoukaiMoveTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.YoukaiVanishTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.home.YoukaiGoHomeTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.home.YoukaiSleepTask;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.InteractWithDoor;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
		board.addRandom(new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());

		board.addSensor(new NearbyPlayersSensor<SmartYoukaiEntity>().setRadius(32).setScanRate(e -> 5));
		board.addSensor(new NearbyLivingEntitySensor<SmartYoukaiEntity>().setRadius(32)
				.setScanRate(self -> self.isAggressive() || self.hasPlayerNearby() ? 10 : 20));
		board.addSensor(new YoukaiUpdateHomeSensor<SmartYoukaiEntity>().setScanRate(e -> 80));
	}

	private void checkBoard() {
		if (board == null) {
			board = new TaskBoard();
			constructTaskBoard(board);
			board.build();
		}
	}

	private Behavior<? super SmartYoukaiEntity>[] buildBehaviors(Activity activity) {
		checkBoard();
		return board.fetch(activity);
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
				new StrafeTarget<>(),
				new YoukaiMoveTask<>(),
				new InteractWithDoor<>()
		);
	}

	@Override
	public BrainActivityGroup<? extends SmartYoukaiEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(
				new YoukaiUpdateTargetTask<>(),
				new YoukaiAttackTask<>(16)
		);
	}

	@Override
	public BrainActivityGroup<SmartYoukaiEntity> getIdleTasks() {
		return new BrainActivityGroup<SmartYoukaiEntity>(Activity.IDLE).priority(10).behaviours(
				buildBehaviors(Activity.IDLE)
		).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
	}

	protected BrainActivityGroup<SmartYoukaiEntity> getRestTasks() {
		return new BrainActivityGroup<SmartYoukaiEntity>(Activity.REST).priority(10).behaviours(
						buildBehaviors(Activity.REST)
				).onlyStartWithMemoryStatus(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT)
				.onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
	}

	protected BrainActivityGroup<SmartYoukaiEntity> getAtHomeTasks() {
		return new BrainActivityGroup<SmartYoukaiEntity>(GLBrains.AT_HOME.get()).priority(10).behaviours(
						buildBehaviors(GLBrains.AT_HOME.get())
				).onlyStartWithMemoryStatus(MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT)
				.onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
	}

	protected BrainActivityGroup<SmartYoukaiEntity> getPlayTasks() {
		return new BrainActivityGroup<SmartYoukaiEntity>(Activity.PLAY).priority(10).behaviours(
				buildBehaviors(Activity.PLAY)
		).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT);
	}

	@Override
	public Map<Activity, BrainActivityGroup<? extends SmartYoukaiEntity>> getAdditionalTasks() {
		return Map.of(Activity.REST, getRestTasks(),
				GLBrains.AT_HOME.get(), getAtHomeTasks(),
				Activity.PLAY, getPlayTasks());
	}

	@Override
	public List<Activity> getActivityPriorities() {
		return List.of(Activity.FIGHT);
	}

	@Override
	public @Nullable SmartBrainSchedule getSchedule() {
		return new SmartBrainSchedule()
				.activityAt(10, GLBrains.AT_HOME.get())
				.activityAt(2000, Activity.IDLE)
				.activityAt(4000, Activity.PLAY)
				.activityAt(8000, Activity.IDLE)
				.activityAt(10000, GLBrains.AT_HOME.get())
				.activityAt(12000, Activity.REST);
	}

	public String getBrainDebugInfo() {
		return getBrain().getActiveNonCoreActivity().map(Activity::getName).orElse("");
	}

}
