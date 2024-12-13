package dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiAttackTask<T extends YoukaiEntity> extends ExtendedBehaviour<T> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
			.hasMemory(MemoryModuleType.ATTACK_TARGET)
			.usesMemory(MemoryModuleType.WALK_TARGET);

	private final int range;
	private int meleeTime;
	private int shootTime;

	public YoukaiAttackTask(int range) {
		this.range = range;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean shouldKeepRunning(T entity) {
		return BrainUtils.getMemory(entity, MemoryModuleType.ATTACK_TARGET) != null;
	}

	@Override
	protected void start(T youkai) {
		meleeTime = 10;
		shootTime = 20;
		youkai.setAggressive(true);
		youkai.navCtrl.setFlying();
	}

	@Override
	protected void stop(T youkai) {
		youkai.setAggressive(false);
		youkai.navCtrl.setWalking();
	}

	@Override
	protected void tick(T youkai) {
		if (shootTime > 0) {
			shootTime--;
		}
		if (meleeTime > 0) {
			meleeTime--;
		}
		if (specialAction()) {
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


	protected void attack(YoukaiEntity youkai, LivingEntity target, double dist, boolean sight) {
		double melee = getMeleeRange();
		if (sight && dist < melee * melee) {
			if (meleeTime <= 0) {
				meleeTime = 20;
				meleeAttack(youkai, target);
			}
		}
		if (shootTime <= 0) {
			shootTime = shoot(target, youkai.targets.getTargets());
		}
	}

	protected void meleeAttack(YoukaiEntity youkai, LivingEntity target) {
		youkai.doHurtTarget(target);
	}

	protected boolean specialAction() {
		return false;
	}

	protected int shoot(LivingEntity target, List<LivingEntity> all) {
		return 20;
	}

	protected double getMeleeRange() {
		return 2;
	}

	public double getShootRange(YoukaiEntity youkai) {
		return youkai.getAttributeValue(Attributes.FOLLOW_RANGE);
	}

}
