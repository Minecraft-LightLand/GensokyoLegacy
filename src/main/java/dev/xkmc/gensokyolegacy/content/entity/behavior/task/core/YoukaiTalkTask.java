package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.module.TalkModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiTalkTask<E extends SmartYoukaiEntity> extends ExtendedBehaviour<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.noMemory(MemoryModuleType.ATTACK_TARGET)
			.hasMemory(GLBrains.MEM_TALK.get());

	public YoukaiTalkTask() {
		super();
		noTimeout();
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean shouldKeepRunning(E entity) {
		return BrainUtils.hasMemory(entity, GLBrains.MEM_TALK.get()) &&
				!BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET);
	}

	@Override
	protected void tick(ServerLevel level, E entity, long gameTime) {
		var player = BrainUtils.getMemory(entity, GLBrains.MEM_TALK.get());
		if (player == null) return;
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(player, true));
	}

	@Override
	protected void stop(E entity) {
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
		BrainUtils.clearMemory(entity, GLBrains.MEM_TALK.get());
		entity.getModule(TalkModule.class).ifPresent(TalkModule::stopTalking);
	}
}
