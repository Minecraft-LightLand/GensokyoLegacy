package dev.xkmc.gensokyolegacy.content.entity.behavior.task.play;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class ItemPickupTask extends ExtendedBehaviour<SmartYoukaiEntity> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.hasMemory(SBLMemoryTypes.NEARBY_ITEMS.get())
			.usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);

	private ItemEntity item;
	private long tire = 0;

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	private boolean isValid(SmartYoukaiEntity e, ItemEntity item) {
		return !item.isRemoved() && !item.getItem().isEmpty() && e.wantsToPickUp(item.getItem());
	}

	protected boolean trackNext(SmartYoukaiEntity entity) {
		var list = BrainUtils.getMemory(entity, SBLMemoryTypes.NEARBY_ITEMS.get());
		if (list == null) return false;
		for (var e : list) {
			if (isValid(entity, e)) {
				this.item = e;
				return true;
			}
		}
		return false;
	}

	protected void setToItem(SmartYoukaiEntity entity) {
		if (item == null || !isValid(entity, item)) return;
		BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(item, 1, 1));
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(item, false));
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, SmartYoukaiEntity entity) {
		if (tire > level.getGameTime()) return false;
		boolean ans = trackNext(entity);
		if (ans) return true;
		tire = level.getGameTime() + 20;
		return false;
	}

	@Override
	protected void start(SmartYoukaiEntity entity) {
		setToItem(entity);
	}

	@Override
	protected boolean shouldKeepRunning(SmartYoukaiEntity entity) {
		if (item == null || !isValid(entity, item)) {
			var ans = trackNext(entity);
			if (ans) setToItem(entity);
			return ans;
		}
		return true;
	}

	@Override
	protected void stop(SmartYoukaiEntity entity) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
	}

}
