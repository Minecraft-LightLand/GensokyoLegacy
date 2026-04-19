package dev.xkmc.gensokyolegacy.content.client.deco;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class DowserRender {

	public static void search(ServerLevel level, BlockPos pos) {
		var cpos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		int r = 4;
		Set<BlockPos> ans = new LinkedHashSet<>();
		for (int ix = -r; ix <= r; ix++) {
			for (int iz = -r; iz <= r; iz++) {
				var chunk = level.getChunk(cpos.x + ix, cpos.z + iz, ChunkStatus.FULL, false);
				if (chunk == null) continue;
				Map<BlockPos, BlockEntity> bss = chunk instanceof ProtoChunk proto ? proto.getBlockEntities() :
						chunk instanceof LevelChunk lc ? lc.getBlockEntities() : Map.of();
				for (var ent : bss.entrySet()) {
					if (ent.getValue() instanceof RandomizableContainerBlockEntity) {
						ans.add(ent.getKey());
					}
				}
			}
		}
	}

}
