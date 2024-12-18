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
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Function;

public class YoukaiCraftTask<E extends SmartYoukaiEntity> extends AbstractHomeHolderTask<E> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(4)
			.noMemory(MemoryModuleType.WALK_TARGET)
			.hasMemory(MemoryModuleType.HOME)
			.noMemory(MemoryModuleType.ATTACK_TARGET)
			.usesMemory(MemoryModuleType.LOOK_TARGET);

	private BlockPos chest;

	private final Function<Boolean, ItemStack> doCraft;
	private final int craftDuration, craftCoolDown;

	private long walkEnd, craftEnd, nextCraft;

	public YoukaiCraftTask(Function<Boolean, ItemStack> doCraft, int craftDuration, int craftCoolDown) {
		this.doCraft = doCraft;
		this.craftDuration = craftDuration;
		this.craftCoolDown = craftCoolDown;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
		if (level.getGameTime() < nextCraft) return false;
		if (!super.checkExtraStartConditions(level, entity)) return false;
		var bed = BedRefData.of(level, entity);
		if (bed.isEmpty() || bed.get().getBedPos() == null) return false;
		chest = home.getContainersAround(bed.get().getBedPos());
		return chest != null && !doCraft.apply(true).isEmpty();
	}

	@Override
	protected void start(ServerLevel level, E entity, long gameTime) {
		BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(chest, 1, 1));
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new BlockPosTracker(chest));
		walkEnd = gameTime + 100;
		craftEnd = 0;
	}

	@Override
	protected boolean canStillUse(ServerLevel level, E entity, long gameTime) {
		if (stopCondition.test(entity)) return false;
		if (!home.isValid()) return false;
		if (!HomeSearchUtil.isValidChest(level, chest)) return false;
		if (craftEnd == 0) {
			if (entity.distanceToSqr(chest.getCenter()) < 4) {
				craftEnd = gameTime + craftDuration;
				return true;
			}
			return gameTime < walkEnd;
		}
		if (craftEnd == gameTime) {
			HomeSearchUtil.put(level, chest, doCraft);
			nextCraft = craftEnd + craftCoolDown;
			BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
			BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
			return false;
		}
		return gameTime < craftEnd;
	}

	@Override
	protected void stop(E entity) {
		walkEnd = 0;
		craftEnd = 0;
		chest = null;
		super.stop(entity);
	}

}
