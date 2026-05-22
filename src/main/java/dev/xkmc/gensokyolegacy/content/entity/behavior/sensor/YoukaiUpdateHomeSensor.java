package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.util.BrainUtils;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Optional;
import java.util.Set;

public class YoukaiUpdateHomeSensor<E extends YoukaiEntity> extends Sensor<E> {

	public YoukaiUpdateHomeSensor() {
		super(80);
	}

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return Set.of(MemoryModuleType.HOME);
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		var pos = get(level, entity);
		if (pos.isPresent()) BrainUtils.setMemory(entity, MemoryModuleType.HOME, pos.get());
		else BrainUtils.clearMemory(entity, MemoryModuleType.HOME);
	}

	private Optional<GlobalPos> get(ServerLevel level, E entity) {
		var home = StructureKey.of(entity).orElse(null);
		if (home == null || !level.dimension().location().equals(home.dim())) return Optional.empty();
		var bed = BedRefData.of(level, home, entity.getType());
		if (bed == null) return Optional.empty();
		var pos = bed.getBedPos();
		if (pos == null) return Optional.empty();
		return Optional.of(new GlobalPos(home.getDim(), pos));
	}

}
