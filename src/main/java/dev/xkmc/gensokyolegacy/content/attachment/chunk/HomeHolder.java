package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record HomeHolder(
		ServerLevel level, LevelChunk chunk, StructureKey key,
		StructureConfig config, StructureAttachment attachment, HomeData data
) {

	@Nullable
	public static HomeHolder of(ServerLevel level, StructureKey key) {
		if (!level.isLoaded(key.pos())) return null;
		var config = level.registryAccess().holderOrThrow(key.getStructure()).getData(GLMeta.STRUCTURE_DATA.reg());
		if (config == null) return null;
		var chunk = level.getChunkAt(key.pos());
		var att = chunk.getData(GLMeta.STRUCTURE.get());
		var home = att.data.computeIfAbsent(key, k -> new HomeData());
		return new HomeHolder(level, chunk, key, config, att, home);
	}

	@Nullable
	public static HomeHolder find(ServerLevel sl, BlockPos pos) {
		if (!sl.isLoaded(pos)) return null;

		// First, check for manually created structures in StructureAttachment
		var chunk = sl.getChunkAt(pos);
		var attachment = chunk.getData(GLMeta.STRUCTURE.get());
		for (var key : attachment.data.keySet()) {
			var holder = of(sl, key);
			if (holder != null) {
				holder.data.init(holder);
				return holder;
			}
		}

		// If not found, check for world-generated structures
		var manager = sl.structureManager();
		var map = manager.getAllStructuresAt(pos);
		var reg = sl.registryAccess().registryOrThrow(Registries.STRUCTURE);
		for (var e : map.keySet()) {
			var start = manager.getStructureWithPieceAt(pos, e);
			if (!start.isValid()) continue;
			var id = reg.getHolder(reg.getId(start.getStructure()));
			if (id.isEmpty()) continue;
			if (start.getPieces().isEmpty()) continue;
			var root = start.getPieces().getFirst().getLocatorPosition();
			return of(sl, new StructureKey(id.get().key(), sl.dimension(), root));
		}
		return null;
	}

	public boolean isValid() {
		return level.isLoaded(key.pos()) && data.checkInit(this);
	}

	public boolean isInRoom(BlockPos pos) {
		if (!data.checkInit(this)) return false;
		return data.getRoomBound(config).isInside(pos);
	}

	@Nullable
	public Vec3 getRandomPosInRoom(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPos(data.getRoomBound(config), e, ans -> !config.isOutside(e.level(), ans));
	}

	@Nullable
	public Vec3 getRandomPosInBound(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPos(data.getTotalBound(), e, ans -> true);
	}

	@Nullable
	public BlockPos getContainersAround(BlockPos pos) {
		if (!data.checkInit(this)) return null;
		return data.getContainerAround(this, pos, 3, 3, 32);
	}

	@Nullable
	public BlockPos getChairsAround(BlockPos pos) {
		if (!data.checkInit(this)) return null;
		return data.getChairAround(this, pos, 3, 3, 12);
	}

	@Nullable
	public BlockPos getWanderCenter() {
		if (!data.checkInit(this)) return null;
		var box = data.getTotalBound();
		return box.getCenter();
	}

	public int getWanderBaseRadius() {
		if (!data.checkInit(this)) return 0;
		var box = data.getTotalBound();
		return Math.min(box.getXSpan() / 2, box.getZSpan() / 2);
	}

	public void tick() {
		if (!data.checkInit(this)) return;
		data.tick(this);
	}

	public List<BlockFix> popFix(int count, FixStage stage) {
		if (!data.checkInit(this)) return List.of();
		return data.popFix(count, stage);
	}

	public int doFix(int count, FixStage stage) {
		if (!data.checkInit(this)) return 0;
		var list = data.popFix(count, stage);
		for (var e : list) {
			e.fix(level);
		}
		return list.size();
	}

	public boolean isBroken() {
		if (!data.checkInit(this)) return false;
		return data.getBrokenCount() >= PerformanceConstants.COMMAND_PLACE_STEP;
	}

}
