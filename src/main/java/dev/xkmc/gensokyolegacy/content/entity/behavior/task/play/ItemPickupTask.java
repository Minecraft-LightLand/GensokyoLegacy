package dev.xkmc.gensokyolegacy.content.entity.behavior.task.play;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;

import java.util.Map;

public class ItemPickupTask extends Behavior<SmartYoukaiEntity> {

	private ItemEntity item;
	private long tire = 0;

	public ItemPickupTask() {
		super(Map.of(
				MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED,
				GLBrains.MEM_ITEMS.get(), MemoryStatus.VALUE_PRESENT,
				MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED
		));
	}

	private boolean isValid(SmartYoukaiEntity e, ItemEntity item) {
		return !item.isRemoved() && !item.getItem().isEmpty() && e.wantsToPickUp(item.getItem());
	}

	protected boolean trackNext(SmartYoukaiEntity entity) {
		var list = BrainUtils.getMemory(entity, GLBrains.MEM_ITEMS.get());
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
	protected void start(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		setToItem(entity);
	}

	@Override
	protected boolean canStillUse(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		if (item == null || !isValid(entity, item)) {
			var ans = trackNext(entity);
			if (ans) setToItem(entity);
			return ans;
		}
		return true;
	}

	@Override
	protected void stop(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
	}

}
