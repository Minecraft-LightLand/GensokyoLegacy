package dev.xkmc.gensokyolegacy.content.attachment.home.core;

import dev.xkmc.gensokyolegacy.content.attachment.home.custom.CustomHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.structure.StructureHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IHomeHolder {

	@Nullable
	static IHomeHolder of(ServerLevel sl, SmartYoukaiEntity entity) {
		var key = StructureKey.of(entity);
		if (key.isEmpty()) return null;
		var ans = StructureHomeHolder.of(sl, key.get());
		if (ans != null) return ans;
		return CustomHomeHolder.of(sl, key.get().pos());
	}

	@Nullable
	static IHomeHolder find(ServerLevel sl, BlockPos pos) {
		if (!sl.isLoaded(pos)) return null;

		// First, check for manually created structures in StructureAttachment
		int r = 1;
		for (int ix = -r; ix <= r; ix++) {
			for (int iz = -r; iz <= r; iz++) {
				var chunk = sl.getChunkAt(pos.offset(ix * 16, 0, iz * 16));
				var att = chunk.getData(GLMeta.STRUCTURE.get());
				for (var pair : att.custom.entrySet()) {
					if (pair.getValue().getTotalBound().isInside(pos)) {
						return new CustomHomeHolder(sl, chunk, StructureKey.custom(sl.dimension(), pair.getKey()), att, pair.getValue());
					}
				}
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
			return StructureHomeHolder.of(sl, new StructureKey(id.get().key(), sl.dimension(), root));
		}
		return null;

	}

	@Nullable
	static IHomeHolder of(ServerLevel sl, StructureKey key) {
		var ans = StructureHomeHolder.of(sl, key);
		if (ans != null) return ans;
		return CustomHomeHolder.of(sl, key.pos());
	}

	StructureKey key();

	boolean isValid();

	boolean supportEntity(EntityType<?> type);

	boolean isInRoom(BlockPos pos);

	@Nullable
	Vec3 getRandomPosInRoom(YoukaiEntity e);

	@Nullable
	Vec3 getRandomPosInBound(YoukaiEntity e);

	@Nullable
	BlockPos getContainersAround(BlockPos pos);

	@Nullable
	BlockPos getChairsAround(BlockPos pos);

	@Nullable
	BlockPos getWanderCenter();

	int getWanderBaseRadius();


	void tick();

	SimplePacketBase toBoundPacket();

	SimplePacketBase getUpdatePacket();

}
