package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;
import java.util.Random;

public final class GenerationConfig {

	public int maxScale;
	public long seed;

	public double wallExtra = 0;
	public double cellExtra = 0;
	public int cacheSize = 16384;

	public LeafManager leaf;

	public GenerationConfig(int maxScale, long seed, LeafManager manager) {
		this.maxScale = maxScale;
		this.seed = seed;
		this.leaf = manager;
	}

	public int maxScale() {
		return maxScale;
	}

	public long seed() {
		return seed;
	}

	public double wallExtra() {
		return wallExtra;
	}

	public double cellExtra() {
		return cellExtra;
	}

	public int cacheSize() {
		return cacheSize;
	}

	@Nullable
	public CellContent getLeaf(RandomSource random, MazeCell3D cell) {
		return leaf.getLeaf(random, cell);
	}

}
