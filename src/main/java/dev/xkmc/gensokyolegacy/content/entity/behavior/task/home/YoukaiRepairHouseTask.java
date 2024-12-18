package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.FixStage;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.PerformanceConstants;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiRepairHouseTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.hasMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	private Pair<BlockPos, BlockState> toFix;

	private long walkEnd;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		if (!super.checkExtraStartConditions(level, entity)) return false;
		var ans = home.popFix(1, FixStage.ALL);
		if (ans.isEmpty()) return false;
		toFix = ans.getFirst();
		return true;
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(toFix.getFirst(), 1, 3));
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(toFix.getFirst()));
		walkEnd = gameTime + 300;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
		if (stopCondition.test(entity)) return false;
		if (!home.isValid() || toFix == null) return false;
		var pos = entity.getEyePosition().subtract(toFix.getFirst().getCenter());
		if (pos.horizontalDistance() < 3 && Math.abs(pos.y) < 4) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
			BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
			entity.swing(InteractionHand.MAIN_HAND);
			level.setBlockAndUpdate(toFix.getFirst(), toFix.getSecond());
			walkEnd = 0;
			if (!entity.hasPlayerNearby() && home.isBroken()) {
				home.doFix(PerformanceConstants.COMMAND_PLACE_STEP, FixStage.ALL);
			}
			return false;
		}
		return gameTime < walkEnd;
	}

	@Override
	protected void stop(E entity) {
		toFix = null;
		walkEnd = 0;
	}

}
