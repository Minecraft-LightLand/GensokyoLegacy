package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class NearbyPlayerSensor<E extends SmartYoukaiEntity> extends AbstractNearbyEntitySensor<Player, E> {

	public NearbyPlayerSensor() {
		super(MemoryModuleType.NEAREST_PLAYERS, Player.class,
				(target, entity) -> target.canBeSeenByAnyone());
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return Set.of(
				MemoryModuleType.NEAREST_PLAYERS,
				MemoryModuleType.NEAREST_VISIBLE_PLAYER,
				MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER
		);
	}

	@Override
	protected void setMemory(ServerLevel level, E entity, List<Player> list) {
		list.sort(Comparator.comparingDouble(entity::distanceToSqr));
		BrainUtils.setMemory(entity, MemoryModuleType.NEAREST_PLAYERS, list);
		list = new ArrayList<>(list);
		list.removeIf(p -> !entity.targets.contains(p) && p.distanceTo(entity) > p.getVisibilityPercent(entity) * 16);
		BrainUtils.setMemory(entity, MemoryModuleType.NEAREST_VISIBLE_PLAYER, list.isEmpty() ? null : list.getFirst());
		list = new ArrayList<>(list);
		list.removeIf(p -> !p.canBeSeenAsEnemy());
		BrainUtils.setMemory(entity, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, list.isEmpty() ? null : list.getFirst());
	}

}
