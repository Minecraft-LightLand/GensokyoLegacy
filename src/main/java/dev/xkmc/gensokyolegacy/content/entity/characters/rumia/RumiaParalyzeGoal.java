package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class RumiaParalyzeGoal extends Behavior<RumiaEntity> {

	public RumiaParalyzeGoal() {
		super(Map.of(
				GLBrains.MEM_DOWN.get(), MemoryStatus.VALUE_PRESENT
		));
	}

	@Override
	protected boolean timedOut(long gameTime) {
		return false;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, RumiaEntity entity, long gameTime) {
		return entity.isBlocked();
	}

	@Override
	protected void start(ServerLevel level, RumiaEntity entity, long gameTime) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
	}
}
