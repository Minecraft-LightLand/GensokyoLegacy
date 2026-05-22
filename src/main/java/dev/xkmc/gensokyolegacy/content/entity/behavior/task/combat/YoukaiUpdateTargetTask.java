package dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class YoukaiUpdateTargetTask<E extends YoukaiEntity> extends Behavior<E> {

	protected long pathfindingAttentionSpan = 200L;

	public YoukaiUpdateTargetTask() {
		super(Map.of(
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED
		));
	}

	@Nullable
	protected LivingEntity getNewTarget(E entity) {
		Brain<?> brain = entity.getBrain();
		var toTarget = entity.targets.getPrimaryTarget();
		if (toTarget != null) return toTarget;
		NearestVisibleLivingEntities list = BrainUtils.getMemory(brain, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
		if (list == null) return null;
		var opt = list.findClosest(entity::wouldInitiateAttack);
		return opt.orElse(null);
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		LivingEntity target = BrainUtils.getTargetOfEntity(entity);
		if (target == null) return;
		if (!entity.targets.isValidTarget(target) || isTiredOfPathing(entity)) {
			target = getNewTarget(entity);
			if (target != null) BrainUtils.setTargetOfEntity(entity, target);
			else {
				entity.setTarget(null);
				BrainUtils.clearMemory(entity, MemoryModuleType.ATTACK_TARGET);
			}
		}
	}

	protected boolean isTiredOfPathing(E entity) {
		if (this.pathfindingAttentionSpan <= 0L) {
			return false;
		} else {
			Long time = BrainUtils.getMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
			return time != null && entity.level().getGameTime() - time > this.pathfindingAttentionSpan;
		}
	}

}
