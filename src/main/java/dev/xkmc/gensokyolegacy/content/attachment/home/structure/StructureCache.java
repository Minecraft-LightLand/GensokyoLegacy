package dev.xkmc.gensokyolegacy.content.attachment.home.structure;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.PerformanceConstants;
import dev.xkmc.gensokyolegacy.content.attachment.home.core.StructureBound;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.LinkedHashMap;

public record StructureCache(BlockState[] palette, int[] raster) {

	public BlockState stateAt(int e) {
		return palette[raster[e]];
	}

	public static class Builder {

		private final ServerLevel level;
		private final StructureBound bound;
		private final LinkedHashMap<BlockState, Integer> palette = new LinkedHashMap<>();
		private final int[] raster;

		private long lastGameTime = 0;
		private int step = 0;

		public Builder(ServerLevel level, BoundingBox bound) {
			this.level = level;
			this.bound = new StructureBound(bound);
			raster = new int[this.bound.getSize()];
			palette.put(Blocks.AIR.defaultBlockState(), 0);
		}

		public void tick() {
			if (level.getGameTime() == lastGameTime) return;
			lastGameTime = level.getGameTime();
			var pos = new BlockPos.MutableBlockPos();
			int maxStep = Math.min(step + PerformanceConstants.SETUP_SCAN, bound.getSize());
			for (; step < maxStep; step++) {
				bound.resolve(pos, step);
				var state = level.getBlockState(pos);
				int id = palette.computeIfAbsent(state, k -> palette.size());
				raster[step] = id;
			}
		}

		public boolean isDone() {
			return step == bound.getSize();
		}

		public StructureCache build() {
			return new StructureCache(palette.keySet().toArray(BlockState[]::new), raster);
		}

	}

}
