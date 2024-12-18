package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiSitTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(4)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.hasMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET)
			.usesMemory(MemoryModuleType.LOOK_TARGET);

	private BlockPos chair;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
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
		if (stopCondition.test(entity)) return false;
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
	protected void stop(E entity) {
		if (entity.isPassenger())
			entity.stopRiding();
		chair = null;
		super.stop(entity);
	}

}
