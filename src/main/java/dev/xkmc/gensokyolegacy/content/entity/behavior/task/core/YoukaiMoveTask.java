package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.behavior.move.CompoundPath;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YoukaiMoveTask<E extends YoukaiEntity> extends ExtendedBehaviour<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(4)
			.hasMemory(MemoryModuleType.WALK_TARGET)
			.noMemory(GLBrains.MEM_PATH.get())
			.usesMemory(MemoryModuleType.PATH)
			.usesMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

	@Nullable
	protected CompoundPath path;
	@Nullable
	protected BlockPos lastTargetPos;
	protected float speedModifier;

	private int leaveGroundTick;

	public YoukaiMoveTask() {
		runFor(entity -> entity.getRandom().nextInt(100) + 150);
		cooldownFor(entity -> entity.getRandom().nextInt(40));
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		Brain<?> brain = entity.getBrain();
		WalkTarget walkTarget = BrainUtils.getMemory(brain, MemoryModuleType.WALK_TARGET);
		if (walkTarget != null &&
				!hasReachedTarget(entity, walkTarget) &&
				attemptNewPath(entity, walkTarget, false)
		) {
			this.lastTargetPos = walkTarget.getTarget().currentBlockPosition();
			return true;
		}
		BrainUtils.clearMemory(brain, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		return false;
	}

	@Override
	protected boolean shouldKeepRunning(E entity) {
		if (this.path == null || this.lastTargetPos == null)
			return false;
		if (entity.getNavigation().isDone())
			return false;
		WalkTarget walkTarget = BrainUtils.getMemory(entity, MemoryModuleType.WALK_TARGET);
		return walkTarget != null && !hasReachedTarget(entity, walkTarget);
	}

	@Override
	protected void start(E entity) {
		BrainUtils.setMemory(entity, MemoryModuleType.PATH, path == null ? null : path.path());
		BrainUtils.setMemory(entity, GLBrains.MEM_PATH.get(), this.path);
		if (path == null) return;
		entity.navCtrl.moveTo(this.path, this.speedModifier);
	}

	@Override
	protected void tick(E entity) {
		CompoundPath path = entity.navCtrl.getPath();
		Brain<?> brain = entity.getBrain();
		if (this.path != path) {
			this.path = path;
			BrainUtils.setMemory(brain, MemoryModuleType.PATH, path == null ? null : path.path());
			BrainUtils.setMemory(brain, GLBrains.MEM_PATH.get(), path);
		}
		if (path != null && this.lastTargetPos != null) {
			WalkTarget target = BrainUtils.getMemory(brain, MemoryModuleType.WALK_TARGET);
			if (target != null && target.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4) {
				if (attemptNewPath(entity, target, hasReachedTarget(entity, target)))
					this.lastTargetPos = target.getTarget().currentBlockPosition();
			}
		}
	}

	@Override
	protected void stop(E entity) {
		Brain<?> brain = entity.getBrain();
		var target = BrainUtils.getMemory(brain, MemoryModuleType.WALK_TARGET);
		if (!entity.getNavigation().isStuck() ||
				!BrainUtils.hasMemory(brain, MemoryModuleType.WALK_TARGET) ||
				target != null && hasReachedTarget(entity, target)
		) this.cooldownFinishedAt = 0;
		entity.getNavigation().stop();
		BrainUtils.clearMemories(brain, MemoryModuleType.WALK_TARGET, MemoryModuleType.PATH, GLBrains.MEM_PATH.get());
		this.path = null;
	}

	protected boolean attemptNewPath(E entity, WalkTarget walkTarget, boolean reachedCurrentTarget) {
		Brain<?> brain = entity.getBrain();
		if (reachedCurrentTarget) {
			BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
			return false;
		}
		Vec3 pos = Vec3.atBottomCenterOf(walkTarget.getTarget().currentBlockPosition());
		entity.getNavigation().moveTo(pos.x, pos.y, pos.z, 0, walkTarget.getSpeedModifier());
		this.path = entity.navCtrl.getPath();
		this.speedModifier = walkTarget.getSpeedModifier();
		if (this.path != null && this.path.path().canReach()) {
			BrainUtils.clearMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
		} else {
			BrainUtils.setMemory(brain, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, entity.level().getGameTime());
		}
		if (this.path != null) return true;
		Vec3 nextPos = DefaultRandomPos.getPosTowards(entity, 10, 7, pos, Mth.HALF_PI);
		if (nextPos != null) {
			entity.getNavigation().moveTo(nextPos.x(), nextPos.y(), nextPos.z(), 0, 1);
			this.path = entity.navCtrl.getPath();
			return this.path != null;
		}
		return false;
	}

	protected boolean hasReachedTarget(E entity, WalkTarget target) {
		return target.getTarget().currentBlockPosition().distManhattan(entity.blockPosition()) <= target.getCloseEnoughDist();
	}

}