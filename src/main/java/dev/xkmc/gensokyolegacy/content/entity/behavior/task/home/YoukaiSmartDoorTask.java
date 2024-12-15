package dev.xkmc.gensokyolegacy.content.entity.behavior.task.home;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.InteractWithDoor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class YoukaiSmartDoorTask<E extends SmartYoukaiEntity> extends InteractWithDoor<E> {

	@Override
	protected void tryOpenDoor(ServerLevel level, E entity, BlockState blockState, BlockPos pos) {
		if (entity.navCtrl.isFlying()) return;
		DoorBlock door = (DoorBlock) blockState.getBlock();
		if (!door.isOpen(blockState))
			door.setOpen(entity, level, blockState, pos, true);
		var gpos = new GlobalPos(level.dimension(), pos);
		var map = BrainUtils.getMemory(entity, MemoryModuleType.DOORS_TO_CLOSE);
		if (map != null) {
			map.add(gpos);
		} else {
			BrainUtils.setMemory(entity, MemoryModuleType.DOORS_TO_CLOSE, new ObjectOpenHashSet<>(Set.of(gpos)));
		}
	}


	protected void checkAndCloseDoors(ServerLevel level, E entity, Set<GlobalPos> doorsToClose, BlockPos prevNodePos, BlockPos nextNodePos) {
		var path = BrainUtils.getMemory(entity, MemoryModuleType.PATH);
		Set<BlockPos> nodes = new LinkedHashSet<>();
		nodes.add(prevNodePos);
		nodes.add(nextNodePos);
		nodes.add(entity.blockPosition());
		if (path != null) {
			int index = path.getNextNodeIndex();
			for (int i = 1; i < 3; i++) {
				if (index + i < path.getNodeCount()) {
					nodes.add(path.getNodePos(index + i));
				}
			}
		}
		for (Iterator<GlobalPos> iterator = doorsToClose.iterator(); iterator.hasNext(); ) {
			GlobalPos doorLocation = iterator.next();
			BlockPos doorPos = doorLocation.pos();
			if (doorLocation.dimension() != level.dimension() || !doorPos.closerToCenterThan(entity.position(), 3)) {
				iterator.remove();
				continue;
			}
			if (nodes.contains(doorPos))
				continue;
			if (checkCloseDoor(level, entity, doorPos)) {
				iterator.remove();
			}
		}
	}

	protected boolean checkCloseDoor(ServerLevel level, E entity, BlockPos doorPos) {
		BlockState doorState = level.getBlockState(doorPos);
		if (!isInteractableDoor(doorState)) return true;
		DoorBlock doorBlock = (DoorBlock) doorState.getBlock();
		if (doorBlock.isOpen(doorState) && !shouldHoldDoorOpenForOthers(entity, doorPos,
				BrainUtils.memoryOrDefault(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES, List::of)))
			doorBlock.setOpen(entity, level, doorState, doorPos, false);
		return true;
	}

}
