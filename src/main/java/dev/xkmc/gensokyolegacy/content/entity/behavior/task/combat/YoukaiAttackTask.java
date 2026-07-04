package dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.List;
import java.util.Map;

public class YoukaiAttackTask<T extends YoukaiEntity> extends Behavior<T> {

	private final int range;
	private int meleeTime;
	private int shootTime;

	public YoukaiAttackTask(int range) {
		this(Map.of(
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
				MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
		), range);
	}

	public YoukaiAttackTask(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int range) {
		super(entryCondition);
		this.range = range;
	}

	@Override
	protected boolean timedOut(long gameTime) {
		return false;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, T entity, long gameTime) {
		return BrainUtils.getMemory(entity, MemoryModuleType.ATTACK_TARGET) != null;
	}

	@Override
	protected void start(ServerLevel level, T youkai, long gameTime) {
		meleeTime = 10;
		shootTime = 20;
		youkai.setAggressive(true);
		if (youkai.mayFly())
			youkai.navCtrl.setFlying();
	}

	@Override
	protected void stop(ServerLevel level, T youkai, long gameTime) {
		youkai.setAggressive(false);
		youkai.navCtrl.setWalking();
	}

	@Override
	protected void tick(ServerLevel level, T youkai, long gameTime) {
		if (shootTime > 0) {
			shootTime--;
		}
		if (meleeTime > 0) {
			meleeTime--;
		}
		if (specialAction(youkai)) {
			return;
		}
		LivingEntity target = youkai.getTarget();
		if (target == null) return;
		boolean sight = youkai.getSensing().hasLineOfSight(target);
		double dist = youkai.distanceToSqr(target);
		double follow = getShootRange(youkai);
		Brain<?> brain = youkai.getBrain();
		if (!sight) {
			if (dist < follow * follow && youkai.getNavigation().isDone()) {
				brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, 1, (int) follow));
			}
		}
		if (sight && dist * 2 < range * range) {
			youkai.getNavigation().stop();
		}
		if (dist > range * range && youkai.getNavigation().isDone()) {
			brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(target, 2, (int) (range * 0.7)));
		}
		if (dist < follow * follow) {
			if (youkai.spellCard == null)
				attack(youkai, target, dist, sight);
			brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
		}
	}

	protected void attack(T youkai, LivingEntity target, double dist, boolean sight) {
		double melee = getMeleeRange(youkai);
		if (sight && dist < melee * melee) {
			if (meleeTime <= 0) {
				meleeTime = 20;
				meleeAttack(youkai, target);
			}
		}
		if (shootTime <= 0) {
			shootTime = shoot(youkai, target, youkai.targets.getTargets());
		}
	}

	protected void meleeAttack(T youkai, LivingEntity target) {
		youkai.doHurtTarget(target);
	}

	protected boolean specialAction(T youkai) {
		return false;
	}

	protected int shoot(T youkai, LivingEntity target, List<LivingEntity> all) {
		return 20;
	}

	protected double getMeleeRange(T youkai) {
		return 2;
	}

	public double getShootRange(T youkai) {
		return youkai.getAttributeValue(Attributes.FOLLOW_RANGE);
	}

}
