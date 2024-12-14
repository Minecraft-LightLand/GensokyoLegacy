package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class RumiaParalyzeGoal extends ExtendedBehaviour<RumiaEntity> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = MemoryTest.builder(1)
			.hasMemories(GLBrains.MEM_DOWN.get());

	public RumiaParalyzeGoal() {
		super();
		noTimeout();
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean shouldKeepRunning(RumiaEntity entity) {
		return entity.isBlocked();
	}

	@Override
	protected void start(RumiaEntity entity) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
	}
}
