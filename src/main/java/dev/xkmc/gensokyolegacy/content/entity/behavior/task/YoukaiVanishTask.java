package dev.xkmc.gensokyolegacy.content.entity.behavior.task;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.List;

public class YoukaiVanishTask extends ExtendedBehaviour<SmartYoukaiEntity> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(2)
			.noMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	private long vanishTime;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, SmartYoukaiEntity entity) {
		if (entity.hasPlayerNearby() || entity.tickCount < 100) {
			vanishTime = 0;
			return false;
		}
		if (vanishTime == 0) {
			var data = CharacterConfig.of(entity.getType());
			int time = data == null ? 100 : data.noPlayerVanishTime();
			vanishTime = level.getGameTime() + time;
			return false;
		}
		return vanishTime <= level.getGameTime();
	}

	@Override
	protected void start(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		entity.discard();
	}

}
