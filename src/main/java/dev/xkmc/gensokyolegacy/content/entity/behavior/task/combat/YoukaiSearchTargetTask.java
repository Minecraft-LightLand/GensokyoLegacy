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

import java.util.Map;

public class YoukaiSearchTargetTask<E extends YoukaiEntity> extends Behavior<E> {

	protected LivingEntity toTarget = null;

	public YoukaiSearchTargetTask() {
		super(Map.of(
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT
		));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
		Brain<?> brain = owner.getBrain();
		this.toTarget = owner.targets.getPrimaryTarget();
		if (this.toTarget == null) {
			NearestVisibleLivingEntities list = BrainUtils.getMemory(brain, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
			if (list == null) return false;
			var opt = list.findClosest(owner::wouldInitiateAttack);
			if (opt.isEmpty()) return false;
			toTarget = opt.get();
		}
		return true;
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		BrainUtils.setTargetOfEntity(entity, this.toTarget);
		BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		this.toTarget = null;
	}

}