package dev.xkmc.gensokyolegacy.content.entity.behavior.sensor;

import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Optional;

public class YoukaiUpdateHomeSensor<E extends YoukaiEntity> extends ExtendedSensor<E> {

	@Override
	public List<MemoryModuleType<?>> memoriesUsed() {
		return List.of(MemoryModuleType.HOME);
	}

	@Override
	public SensorType<YoukaiUpdateHomeSensor<?>> type() {
		return GLBrains.SN_HOME.get();
	}

	@Override
	protected void doTick(ServerLevel level, E entity) {
		var pos = get(level, entity);
		if (pos.isPresent()) BrainUtils.setMemory(entity, MemoryModuleType.HOME, pos.get());
		else BrainUtils.clearMemory(entity, MemoryModuleType.HOME);
	}

	private Optional<GlobalPos> get(ServerLevel level, E entity) {
		var opt = entity.getModule(HomeModule.class);
		if (opt.isEmpty()) return Optional.empty();
		var home = opt.get().home();
		if (home == null || !level.dimension().location().equals(home.dim())) return Optional.empty();
		var bed = IndexStorage.get(level).getOrCreate(home).bedOf(entity.getType());
		if (bed == null) return Optional.empty();
		var pos = bed.getBedPos();
		if (pos == null) return Optional.empty();
		return Optional.of(new GlobalPos(home.getDim(), pos));
	}

}
