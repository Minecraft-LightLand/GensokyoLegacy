package dev.xkmc.gensokyolegacy.content.attachment.dream;

import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import org.jetbrains.annotations.Nullable;

public record DreamChunkHolder(LevelChunk chunk, DreamChunkAttachment data) {

	@Nullable
	public static DreamChunkHolder of(Level level, int x, int z) {
		var chunk = level.getChunk(x, z, ChunkStatus.FULL, false);
		if (chunk == null) return null;
		if (chunk instanceof ImposterProtoChunk im)
			chunk = im.getWrapped();
		if (!(chunk instanceof LevelChunk c)) return null;
		var data = GLMeta.DREAM.type().getOrCreate(c);
		data.init(c);
		return new DreamChunkHolder(c, data);
	}

	public void tick() {
		data.init(chunk);
		data.tick(chunk);
	}

	public void alive(Level level, BlockPos pos) {
		data.init(chunk);
		int max = DreamChunkAttachment.MAX_STABILITY;
		int maxSqr = max * max;
		var sec = SectionPos.of(pos);
		for (int x = -max; x <= max; x++) {
			for (int z = -max; z <= max; z++) {
				int hor = x * x + z * z;
				if (hor >= maxSqr) continue;
				DreamChunkHolder holder;
				if (x == 0 && z == 0) holder = this;
				else holder = of(level, sec.x() + x, sec.z() + z);
				if (holder == null) continue;
				for (int y = -max; y <= max; y++) {
					int dist = hor + y * y;
					if (dist >= maxSqr) continue;
					int index = sec.getY() + y - holder.chunk.getMinSection();
					if (index < 0 || index >= holder.data.sectionData.length) continue;
					var data = holder.data.sectionData[index];
					data.update(max - (int) Math.sqrt(dist));
					holder.chunk.setUnsaved(true);
				}
			}
		}
	}
}
