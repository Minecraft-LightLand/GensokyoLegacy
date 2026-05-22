package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class YoukaiSleepTask extends Behavior<YoukaiEntity> {

	@Nullable
	private GlobalPos pos = null;
	private long desperateSleepyTime = 0;
	private long nextOkStartTime = 0;

	public YoukaiSleepTask() {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT
		));
	}

	@Override
	protected boolean canStillUse(ServerLevel level, YoukaiEntity entity, long gameTime) {
		return entity.getBrain().isActive(Activity.REST);
	}

	@Override
	protected void start(ServerLevel level, YoukaiEntity entity, long gameTime) {
		if (gameTime < nextOkStartTime) return;
		pos = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
		if (pos == null || !level.dimension().equals(pos.dimension())) return;
		desperateSleepyTime = gameTime + 1200;
		if (entity.distanceToSqr(pos.pos().getCenter()) > 2) {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos.pos(), 1, 1));
		}
	}

	@Override
	protected void stop(ServerLevel level, YoukaiEntity entity, long gameTime) {
		if (!entity.isSleeping()) return;
		entity.stopSleeping();
		this.nextOkStartTime = gameTime + 40L;
		this.desperateSleepyTime = 0;
	}

	@Override
	protected void tick(ServerLevel level, YoukaiEntity entity, long gameTime) {
		if (pos == null) return;
		if (entity.isSleeping()) return;
		if (desperateSleepyTime > 0 && gameTime > desperateSleepyTime) {
			if (level.isLoaded(pos.pos())) {
				entity.moveTo(pos.pos().getCenter());
			} else {
				BedRefData.of(level, entity).ifPresent(bed -> bed.removeEntity(level, entity));
			}
			desperateSleepyTime = 0;
		}
		if (entity.distanceToSqr(pos.pos().getCenter()) < 2) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
			entity.getNavigation().stop();
			var state = entity.level().getBlockState(pos.pos());
			if (!state.hasProperty(BedBlock.PART)) {
				pos = null;
				return;
			}
			if (state.getValue(BedBlock.PART) == BedPart.HEAD) {
				entity.startSleeping(pos.pos().relative(state.getValue(BedBlock.FACING).getOpposite()));
			} else {
				entity.startSleeping(pos.pos());
			}
		} else if (!BrainUtils.hasMemory(entity, MemoryModuleType.WALK_TARGET)) {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos.pos(), 1, 1));
		}
	}

	@Override
	protected boolean timedOut(long gameTime) {
		return false;
	}

}
