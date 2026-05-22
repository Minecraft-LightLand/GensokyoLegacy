package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import dev.xkmc.gensokyolegacy.content.entity.module.TalkModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class YoukaiTalkTask<E extends SmartYoukaiEntity> extends Behavior<E> {

	public YoukaiTalkTask() {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
				GLBrains.MEM_TALK.get(), MemoryStatus.VALUE_PRESENT
		));
	}

	@Override
	protected boolean timedOut(long gameTime) {
		return false;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
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
	protected void stop(ServerLevel level, E entity, long gameTime) {
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
		BrainUtils.clearMemory(entity, GLBrains.MEM_TALK.get());
		entity.getModule(TalkModule.class).ifPresent(TalkModule::stopTalking);
	}

}
