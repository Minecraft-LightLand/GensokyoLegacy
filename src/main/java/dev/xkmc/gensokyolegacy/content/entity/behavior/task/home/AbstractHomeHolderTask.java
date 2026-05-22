package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.MustBeInvokedByOverriders;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractHomeHolderTask<E extends SmartYoukaiEntity> extends Behavior<E> {

	protected IHomeHolder home;
	protected BiFunction<E, Vec3, Float> speedModifier = (entity, targetPos) -> 1f;
	protected int cooldown = 0;
	protected Function<E, Integer> cooldownProvider = e -> 0;

	public AbstractHomeHolderTask(Map<MemoryModuleType<?>, MemoryStatus> entryCondition) {
		super(entryCondition);
	}

	public AbstractHomeHolderTask(Map<MemoryModuleType<?>, MemoryStatus> entryCondition, int minDur, int maxDur) {
		super(entryCondition, minDur, maxDur);
	}

	public AbstractHomeHolderTask<E> cooldownFor(Function<E, Integer> function) {
		this.cooldownProvider = function;
		return this;
	}

	public AbstractHomeHolderTask<E> speedModifier(float modifier) {
		return speedModifier((entity, targetPos) -> modifier);
	}

	public AbstractHomeHolderTask<E> speedModifier(BiFunction<E, Vec3, Float> function) {
		this.speedModifier = function;
		return this;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		if (cooldown > entity.tickCount) return false;
		var pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
		if (pos == null || !level.dimension().equals(pos.dimension())) return false;
		if (!entity.isWithinRestriction()) return false;
		updateHome(level, entity);
		return home != null && home.isValid();
	}

	private void updateHome(ServerLevel level, E entity) {
		this.home = IHomeHolder.of(level, entity);
	}

	@MustBeInvokedByOverriders
	@Override
	protected void stop(ServerLevel level, E entity, long gameTime) {
		home = null;
		cooldown = entity.tickCount + cooldownProvider.apply(entity);
	}

}
