package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YoukaiGoHomeTask<E extends SmartYoukaiEntity> extends ExtendedBehaviour<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.hasMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET);

	public YoukaiGoHomeTask() {
	}

	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		var home = BrainUtils.getMemory(entity, MemoryModuleType.HOME);
		if (home == null || !level.dimension().equals(home.dimension())) return false;
		return !entity.isWithinRestriction();
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		Vec3 targetPos = this.getTargetPos(level, entity);
		if (targetPos == null) {
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		} else {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET,
					new WalkTarget(targetPos, 1, 0));
		}
	}

	protected @Nullable Vec3 getTargetPos(ServerLevel sl, E entity) {
		if (entity.isWithinRestriction()) return null;
		IHomeHolder home = IHomeHolder.of(sl, entity);
		if (home == null) {
			return BedRefData.of(sl, entity).map(BedRefData::getBedPos)
					.map(BlockPos::getCenter).orElse(null);
		}
		return home.getRandomPosInRoom(entity);
	}

}