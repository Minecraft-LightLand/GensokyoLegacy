package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;
import java.util.function.Predicate;

public class YoukaiFindPreySensor<T extends SmartYoukaiEntity> extends Sensor<T> {

	private final Predicate<T> time;

	public YoukaiFindPreySensor() {
		this(e -> false);
	}

	public YoukaiFindPreySensor(Predicate<T> time) {
		super(20);
		this.time = time;
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return Set.of(GLBrains.MEM_PREY.get());
	}

	@Override
	protected void doTick(ServerLevel level, T entity) {
		if (!time.test(entity)) return;
		var list = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
		if (list == null) return;
		var opt = list.findClosest(e -> entity.combatManager.targetKind(e).isPrey());
		if (opt.isEmpty()) return;
		BrainUtils.setMemory(entity, GLBrains.MEM_PREY.get(), opt.get());
	}

}
