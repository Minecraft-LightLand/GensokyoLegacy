package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.IFixableHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.PerformanceConstants;
import dev.xkmc.gensokyolegacy.content.attachment.home.structure.BlockFix;
import dev.xkmc.gensokyolegacy.content.attachment.home.structure.FixStage;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

import java.util.Map;

public class YoukaiRepairHouseTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private BlockFix toFix;

	private long walkEnd;

	public YoukaiRepairHouseTask() {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
				MemoryModuleType.HOME, MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT
		));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		if (!super.checkExtraStartConditions(level, entity)) return false;
		if (!(home instanceof IFixableHomeHolder fixable)) return false;
		var ans = fixable.popFix(1, FixStage.ALL);
		if (ans.isEmpty()) return false;
		toFix = ans.getFirst();
		return true;
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(toFix.pos(), 1, 3));
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(toFix.pos()));
		walkEnd = gameTime + 300;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
		if (!home.isValid() || toFix == null) return false;
		var pos = entity.getEyePosition().subtract(toFix.pos().getCenter());
		if (pos.horizontalDistance() < 3 && Math.abs(pos.y) < 4) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
			BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
			entity.swing(InteractionHand.MAIN_HAND);
			toFix.fix(level);
			walkEnd = 0;
			if (!entity.hasPlayerNearby() && home instanceof IFixableHomeHolder fixable && fixable.isBroken()) {
				fixable.doFix(PerformanceConstants.COMMAND_PLACE_STEP, FixStage.ALL);
			}
			return false;
		}
		return gameTime < walkEnd;
	}

	@Override
	protected void stop(ServerLevel level, E entity, long gameTime) {
		toFix = null;
		walkEnd = 0;
		super.stop(level, entity, gameTime);
	}

}
