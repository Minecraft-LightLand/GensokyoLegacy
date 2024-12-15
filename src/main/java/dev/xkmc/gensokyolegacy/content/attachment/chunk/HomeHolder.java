package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public record HomeHolder(
		ServerLevel level, LevelChunk chunk, StructureKey key,
		StructureConfig config, StructureAttachment attachment, HomeData data
) {

	@Nullable
	public static HomeHolder of(ServerLevel level, StructureKey key) {
		if (!level.isLoaded(key.pos())) return null;
		var config = level.registryAccess().holderOrThrow(key.getStructure()).getData(GLMisc.STRUCTURE_DATA.reg());
		if (config == null) return null;
		var chunk = level.getChunkAt(key.pos());
		var att = chunk.getData(GLMisc.STRUCTURE.get());
		var home = att.data.computeIfAbsent(key, k -> new HomeData());
		return new HomeHolder(level, chunk, key, config, att, home);
	}

	public boolean isValid() {
		return level.isLoaded(key.pos()) && data.checkInit(this);
	}

	@Nullable
	public Vec3 getRandomPosInRoom(YoukaiEntity e) {
		if (!data.checkInit(this)) return null;
		return HomeSearchUtil.getRandomPosInRoom(config, data.getRoomBound(config), e);
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

}
