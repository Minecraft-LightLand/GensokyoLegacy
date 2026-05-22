package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;

public class YoukaiSitTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private BlockPos chair;

	public YoukaiSitTask(int minDur, int maxDur) {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
		),minDur, maxDur);
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		if (entity.isPassenger()) return false;
		if (!super.checkExtraStartConditions(level, entity)) return false;
		var bed = BedRefData.of(level, entity);
		if (bed.isEmpty() || bed.get().getBedPos() == null) return false;
		chair = home.getChairsAround(bed.get().getBedPos());
		return chair != null;
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(chair, 1, 1));
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(chair));
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
		if (!home.isValid()) return false;
		if (!HomeSearchUtil.isValidChair(level, chair)) return false;
		if (entity.isPassenger()) return true;
		if (entity.distanceToSqr(chair.getCenter()) < 2) {
			HomeSearchUtil.setSitting(level, chair, entity);
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
			BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
			return entity.isPassenger();
		}
		return true;
	}

	@Override
	protected void stop(ServerLevel level, E entity, long gameTime) {
		if (entity.isPassenger())
			entity.stopRiding();
		chair = null;
		super.stop(level, entity, gameTime);
	}

}
