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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
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
import net.tslat.smartbrainlib.util.BrainUtils;
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

	// setup

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
		board.addAlways(new YoukaiFetchTargetTask<>(), GLBrains.TALK.get(), GLBrains.AT_HOME.get(), Activity.REST);
		board.addAlways(new YoukaiSearchTargetTask<>(), Activity.IDLE, Activity.PLAY);
		board.addAlways(new YoukaiVanishTask(), Activity.IDLE, Activity.PLAY);
		board.addFirst(0, new YoukaiSleepTask(), Activity.REST);
		board.addFirst(0, new YoukaiTalkTask<>(), GLBrains.TALK.get());
		board.addFirst(100, new YoukaiGoHomeTask<>(), Activity.IDLE, GLBrains.AT_HOME.get());
		board.addFirst(200, new YoukaiRepairHouseTask<>(), GLBrains.AT_HOME.get());
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

		board.addScheduledActivity(Activity.REST, MemoryModuleType.HOME);
		board.addScheduledActivity(GLBrains.AT_HOME.get(), MemoryModuleType.HOME);
		board.addScheduledActivity(Activity.PLAY, null);
		board.addScheduledActivity(Activity.IDLE, null);
		board.addPrioritizedActivity(GLBrains.TALK.get(), GLBrains.MEM_TALK.get(), 100);
	}

	private void checkBoard() {
		if (board == null) {
			board = new TaskBoard();
			constructTaskBoard(board);
			board.build();
		}
	}

	//brain

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

	// misc

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

	@Override
	public boolean mayInteract(Player player) {
		if (!super.mayInteract(player)) return false;
		if (level().isClientSide()) {
			return !isSleeping() && !isAggressive();
		}
		var act = getActivity();
		return act != Activity.REST && act != Activity.FIGHT;
	}

	@Override
	public void setTalkTo(ServerPlayer player, int time) {
		getNavigation().stop();
		BrainUtils.clearMemory(this, MemoryModuleType.WALK_TARGET);
		if (time < 0)
			BrainUtils.setMemory(this, GLBrains.MEM_TALK.get(), player);
		else getBrain().setMemoryWithExpiry(GLBrains.MEM_TALK.get(), player, time);
	}

}
