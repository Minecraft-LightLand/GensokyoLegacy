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
		return new DreamChunkHolder(c, data);
	}

	public void markDirty() {
		chunk.setUnsaved(true);
	}

	public void tick() {
		data.tick(chunk);
	}

	public void alive(Level level, BlockPos pos) {
		int max = DreamChunkAttachment.MAX_STABILITY;
		var sec = SectionPos.of(pos);
		for (int x = -max; x <= max; x++) {
			for (int z = -max; z <= max; z++) {
				int val = max - Math.abs(x) - Math.abs(z);
				if (val <= 0) continue;
				DreamChunkHolder holder;
				if (x == 0 && z == 0) holder = this;
				else holder = of(level, sec.x() + x, sec.z() + z);
				if (holder == null) continue;
				for (int y = -max; y <= max; y++) {
					int sval = val - Math.abs(y);
					if (sval <= 0) continue;
					int index = y - holder.chunk.getMinSection();
					if (index < 0 || index >= holder.data.sectionData.length) continue;
					var data = holder.data.sectionData[index];
					data.update(sval);
					holder.chunk.setUnsaved(true);
				}
			}
		}
	}
}
