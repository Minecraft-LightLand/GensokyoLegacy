package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

public abstract class AbstractHomeHolderTask<T extends SmartYoukaiEntity> extends ExtendedBehaviour<T> {

	protected HomeHolder home;

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, T entity) {
		var pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
		if (pos == null || !level.dimension().equals(pos.dimension())) return false;
		if (!entity.isWithinRestriction()) return false;
		updateHome(level, entity);
		return home != null && home.isValid();
	}

	private void updateHome(ServerLevel level, T entity) {
		this.home = entity.getModule(HomeModule.class)
				.map(HomeModule::home)
				.map(e -> HomeHolder.of(level, e))
				.orElse(null);
	}

	@Override
	protected void stop(T entity) {
		home = null;
	}

}
