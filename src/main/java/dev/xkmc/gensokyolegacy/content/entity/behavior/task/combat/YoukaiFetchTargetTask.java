package dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiFetchTargetTask<E extends YoukaiEntity> extends ExtendedBehaviour<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	protected LivingEntity toTarget = null;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E owner) {
		this.toTarget = owner.targets.getPrimaryTarget();
		return this.toTarget != null;
	}

	@Override
	protected void start(E entity) {
		BrainUtils.setTargetOfEntity(entity, this.toTarget);
		BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		this.toTarget = null;
	}

}