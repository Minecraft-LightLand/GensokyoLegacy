package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class AbstractStayTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	public AbstractStayTask() {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT
		));
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		Vec3 targetPos = this.getTargetPos(entity);
		if (targetPos == null) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		} else {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET,
					new WalkTarget(targetPos, speedModifier.apply(entity, targetPos), 0));
		}
	}

	protected abstract @Nullable Vec3 getTargetPos(E entity);

}