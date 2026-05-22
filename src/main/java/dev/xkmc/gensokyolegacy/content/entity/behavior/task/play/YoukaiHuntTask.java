package dev.xkmc.gensokyolegacy.content.entity.behavior.task.play;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;

public class YoukaiHuntTask extends Behavior<SmartYoukaiEntity> {

	private final int distance;

	private LivingEntity target;

	private int cooldown = 0;

	public YoukaiHuntTask(int distance) {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
				GLBrains.MEM_PREY.get(), MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
		));
		this.distance = distance;
	}

	@Override
	protected boolean timedOut(long gameTime) {
		return false;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		if (target != null && entity.targets.isValidTarget(target)) {
			return true;
		}
		BrainUtils.clearMemory(entity, GLBrains.MEM_PREY.get());
		return false;
	}

	@Override
	protected void start(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		target = BrainUtils.getMemory(entity, GLBrains.MEM_PREY.get());
		cooldown = 0;
		if (target == null) return;
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
	}

	@Override
	protected void stop(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
		target = null;
		cooldown = 0;
	}

	@Override
	protected void tick(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		if (target == null) return;
		double dist = entity.distanceTo(target);
		if (dist > distance * 1.5 && !BrainUtils.hasMemory(entity, MemoryModuleType.WALK_TARGET)) {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(target, 1, distance));
		}
		if (dist > distance * 2) return;
		if (cooldown > 0) {
			cooldown--;
			return;
		}
		cooldown = entity.combatManager.doPreyAttack(target);
	}

}
