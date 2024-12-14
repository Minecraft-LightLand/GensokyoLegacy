package dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YoukaiUpdateTargetTask<E extends YoukaiEntity> extends ExtendedBehaviour<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
			.hasMemory(MemoryModuleType.ATTACK_TARGET)
			.usesMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

	protected long pathfindingAttentionSpan = 200L;

	public YoukaiUpdateTargetTask() {
	}

	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
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

	protected void start(E entity) {
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
