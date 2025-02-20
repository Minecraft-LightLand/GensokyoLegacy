package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.CellPos;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.MazeAxis;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.MazeDirection;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.WallPos;
import net.minecraft.util.RandomSource;

public class MazeCell3D {

	public final CellPos pos;
	public final CellContent content;

	private final long seed;
	private final GenerationHelper helper;
	private final MazeWall3D[] walls;

	private Internal internal;

	public MazeCell3D(GenerationHelper helper, CellPos pos, long seed, MazeWall3D[] walls) {
		this.helper = helper;
		this.pos = pos;
		this.seed = seed;
		this.walls = walls;
		helper.cellCount++;
		int openWall = 0;
		for (MazeWall3D wall : walls) {
			if (wall.open) {
				openWall++;
			}
		}
		content = openWall != 1 ? null : helper.getLeaf(this, seed);
	}


	public RandomSource getFrameRandom() {
		return helper.getContentRandom(seed);
	}

	public RandomSource getContentRandom() {
		return helper.getContentRandom(seed);
	}

	/**
	 * get the child at the specified (3 bit) index
	 */
	public MazeCell3D loadChild(int index) {
		if (content != null) {
			return this;
		}
		if (internal == null) {
			internal = new Internal(this);
		}
		return internal.children[index];
	}

	public boolean isLeaf() {
		return content != null;
	}

	public MazeWall3D getWall(MazeDirection dire) {
		return walls[dire.ordinal()];
	}

	private static class Internal {

		private static final int[][] locator;

		static {
			locator = new int[8][6];
			for (int i = 0; i < 8; i++) {
				int dx = i & 1;
				int dy = (i >> 1) & 1;
				int dz = (i >> 2) & 1;
				for (int j = 0; j < 6; j++) {
					MazeDirection dire = MazeDirection.values()[j];
					int offset = ((i >> dire.axis.ordinal()) & 1) + ((dire.factor + 1) >> 1);
					int min = MazeDirection.getDirection(dire.axis, -1).ordinal();
					int max = MazeDirection.getDirection(dire.axis, 1).ordinal();
					int faceIndex = switch (dire.axis) {
						case X -> dy | dz << 1;
						case Y -> dx | dz << 1;
						case Z -> dx | dy << 1;
					};
					if (offset == 0) {
						locator[i][j] = min << 2 | faceIndex;
					} else if (offset == 2) {
						locator[i][j] = max << 2 | faceIndex;
					} else {
						locator[i][j] = 24 + (dire.axis.ordinal() << 2) + faceIndex;
					}
				}
			}
		}

		private static void locate(MazeWall3D[] outer, MazeWall3D[] inner, MazeWall3D[][] result) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 6; j++) {
					int loc = locator[i][j];
					result[i][j] = loc < 24 ? outer[loc >> 2].loadChild(loc & 3) : inner[loc - 24];
				}
			}
		}

		private final MazeCell3D[] children;

		private Internal(MazeCell3D holder) {
			long[] seeds = new long[22];
			holder.helper.getChildrenSeeds(holder.seed, seeds);
			int wallState = holder.helper.randomizeCellInternalState(seeds[20]);
			MazeWall3D[] walls = new MazeWall3D[12];
			int scale = holder.pos.scale() - 1;
			for (int i = 0; i < 3; i++) {
				MazeAxis axis = MazeAxis.values()[i];
				WallPos raw = holder.pos.getWallPos(MazeDirection.getDirection(axis, -1))
						.offset((long) axis.x << scale, (long) axis.y << scale, (long) axis.z << scale, 0);
				for (int j = 0; j < 4; j++) {
					int du = (j & 1) << scale;
					int dv = ((j >> 1) & 1) << scale;
					WallPos pos = raw.offset(du, dv, -1);
					int index = i << 2 | j;
					boolean state = ((wallState >> index) & 1) == 1;
					walls[index] = new MazeWall3D(holder.helper, pos, state, seeds[index]);
				}
			}
			children = new MazeCell3D[8];
			MazeWall3D[][] subWalls = new MazeWall3D[8][6];
			locate(holder.walls, walls, subWalls);
			for (int i = 0; i < 8; i++) {
				int dx = (i & 1) << scale;
				int dy = ((i >> 1) & 1) << scale;
				int dz = ((i >> 2) & 1) << scale;
				CellPos subPos = new CellPos(holder.pos.pos().offset(dx, dy, dz), holder.pos.scale() - 1);
				children[i] = new MazeCell3D(holder.helper, subPos, seeds[i + 12], subWalls[i]);
			}
		}

	}

}
