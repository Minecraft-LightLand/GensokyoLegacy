package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiFunction;

public class YoukaiStayHomeTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.hasMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	protected BiFunction<E, Vec3, Float> speedModifier = (entity, targetPos) -> 1f;


	public YoukaiStayHomeTask() {
	}

	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	public YoukaiStayHomeTask<E> speedModifier(float modifier) {
		return speedModifier((entity, targetPos) -> modifier);
	}

	public YoukaiStayHomeTask<E> speedModifier(BiFunction<E, Vec3, Float> function) {
		this.speedModifier = function;
		return this;
	}

	protected void start(E entity) {
		Vec3 targetPos = this.getTargetPos(entity);
		if (targetPos == null) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		} else {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET,
					new WalkTarget(targetPos, speedModifier.apply(entity, targetPos), 0));
		}
	}

	protected @Nullable Vec3 getTargetPos(E entity) {
		if (entity.isWithinRestriction()) return null;
		if (home == null || !home.isValid()) return null;
		return home.getRandomPosInRoom(entity);
	}

}