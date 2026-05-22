package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;

public class YoukaiLookAtTarget extends LookAtTargetSink {

	public YoukaiLookAtTarget(int minDuration, int maxDuration) {
		super(minDuration, maxDuration);
	}

	@Override
	protected boolean canStillUse(ServerLevel level, Mob entity, long gameTime) {
		return !entity.isSleeping() && super.canStillUse(level, entity, gameTime);
	}

}
