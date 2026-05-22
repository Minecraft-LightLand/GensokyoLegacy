package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.Map;

public class YoukaiVanishTask extends Behavior<SmartYoukaiEntity> {

	private long vanishTime;

	public YoukaiVanishTask() {
		super(Map.of(
				MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT
		));
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
