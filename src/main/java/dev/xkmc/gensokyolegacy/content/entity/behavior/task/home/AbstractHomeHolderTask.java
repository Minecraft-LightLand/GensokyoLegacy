package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.BiFunction;

public abstract class AbstractHomeHolderTask<E extends SmartYoukaiEntity> extends ExtendedBehaviour<E> {

	protected IHomeHolder home;
	protected BiFunction<E, Vec3, Float> speedModifier = (entity, targetPos) -> 1f;

	public AbstractHomeHolderTask<E> speedModifier(float modifier) {
		return speedModifier((entity, targetPos) -> modifier);
	}

	public AbstractHomeHolderTask<E> speedModifier(BiFunction<E, Vec3, Float> function) {
		this.speedModifier = function;
		return this;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		var pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
		if (pos == null || !level.dimension().equals(pos.dimension())) return false;
		if (!entity.isWithinRestriction()) return false;
		updateHome(level, entity);
		return home != null && home.isValid();
	}

	private void updateHome(ServerLevel level, E entity) {
		this.home = IHomeHolder.of(level, entity);
	}

	@Override
	protected void stop(E entity) {
		home = null;
	}

}
