package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.CubeEdge;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeWall3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellLoaderChain;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.InfiniMaze;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.Set;
import java.util.TreeSet;

public class ChunkFiller {

	public enum Step {
		FRAME, CONTENT
	}

	private final FrameConfig blocks;
	private final int cellWidth;
	private final int heightInCell;
	private final int xzCount;

	public ChunkFiller(int cellWidth, int scale, FrameConfig blocks) {
		this.cellWidth = cellWidth;
		this.blocks = blocks;
		this.heightInCell = 1 << scale;
		this.xzCount = 16 / cellWidth;
	}

	public void fillChunk(InfiniMaze maze, ChunkPos pos, ChunkAccess access, Step step) {
		Set<CellPos> complete = new TreeSet<>();
		CellLoaderChain prev = null;
		for (long y = 0; y < heightInCell; y++) {
			for (long x = 0; x < xzCount; x++) {
				for (long z = 0; z < xzCount; z++) {
					long px = x | (long) pos.x * xzCount;
					long pz = z | (long) pos.z * xzCount;
					prev = maze.getCell(prev, new BasePos(px, y, pz));
					MazeCell3D cell = prev.load();
					if (complete.contains(cell.pos)) continue;
					if (step == Step.FRAME) {
						fillCell(cell, pos, access);
					} else {
						fillCellContent(cell, pos, access);
					}
					complete.add(cell.pos);
				}
			}
		}
	}

	private void fillCellContent(MazeCell3D cell, ChunkPos pos, ChunkAccess access) {
		if (cell.content == null) return;
		BasePos c0 = new BasePos((long) pos.x << 4, 0, (long) pos.z << 4);
		BasePos c1 = new BasePos((long) (pos.x + 1) << 4, (long) heightInCell * cellWidth, (long) (pos.z + 1) << 4);
		BoundBox boxC = new BoundBox(c0, c1);
		var p0 = cell.pos.pos().scale(cellWidth);
		var w = (long) cellWidth << cell.pos.scale();
		var p1 = p0.offset(w, w, w);
		BoundBox box = new BoundBox(p0, p1);
		cell.content.generate(box, boxC, access, cell.getContentRandom());
	}

	private void fillCell(MazeCell3D cell, ChunkPos pos, ChunkAccess access) {
		BasePos c0 = new BasePos((long) pos.x << 4, 0, (long) pos.z << 4);
		BasePos c1 = new BasePos((long) (pos.x + 1) << 4, (long) heightInCell * cellWidth, (long) (pos.z + 1) << 4);
		BoundBox boxC = new BoundBox(c0, c1);
		for (CubeEdge edge : CubeEdge.EDGES) {
			BasePos start = new BasePos(edge.x(), edge.y(), edge.z()).scale(1 << cell.pos.scale());
			BasePos p0 = cell.pos.pos().offset(start.x(), start.y(), start.z()).scale(cellWidth);
			BasePos p1 = p0.offset(MazeDirection.getDirection(edge.axis(), 1), (long) cellWidth << cell.pos.scale());
			fillSolidBox(boxC.intersect(new BoundBox(p0, p1).inflate(1, 1, 1)), blocks.hard(), access);
		}
		for (MazeDirection dire : MazeDirection.values()) {
			MazeWall3D wall = cell.getWall(dire);
			fillWallRecursive(boxC, wall, cell.pos.scale() == 0 ? blocks.wall() : blocks.hard(), access);
		}
	}

	private void fillWallRecursive(BoundBox chunk, MazeWall3D wall, BlockState block, ChunkAccess access) {
		BoundBox boxW = new BoundBox(wall.pos.pos(), wall.pos.getMaxEnd())
				.inflate(cellWidth).inflate(-1, -1, -1)
				.inflate(MazeDirection.getDirection(wall.pos.normal(), 1), 2);
		BoundBox inter = chunk.intersect(boxW);
		if (inter.size() > 0) {
			if (wall.open) {
				if (wall.pos.scale() == 0)
					return;
				for (int i = 0; i < 4; i++) {
					MazeWall3D subWall = wall.loadChild(i);
					fillWallRecursive(chunk, subWall, block, access);
				}
				long len = (long) 1 << (wall.pos.scale() - 1);
				WallPos e0 = wall.pos.offset(len, 0, 0);
				WallPos e1 = wall.pos.offset(0, len, 0);
				WallPos e2 = e0.offset(0, len << 1, 0);
				WallPos e3 = e1.offset(len << 1, 0, 0);
				BoundBox b0 = new BoundBox(e0.pos(), e2.pos()).inflate(cellWidth).inflate(1, 1, 1);
				BoundBox b1 = new BoundBox(e1.pos(), e3.pos()).inflate(cellWidth).inflate(1, 1, 1);
				fillSolidBox(chunk.intersect(b0), block, access);
				fillSolidBox(chunk.intersect(b1), block, access);
			} else {
				fillSolidBox(inter, block, access);
			}
		}
	}

	private void fillSolidBox(BoundBox inter, BlockState block, ChunkAccess access) {
		if (inter.size() <= 0)
			return;
		BlockPos.MutableBlockPos bp = new BlockPos.MutableBlockPos();
		for (long x = inter.p0().x(); x < inter.p1().x(); x++) {
			for (long z = inter.p0().z(); z < inter.p1().z(); z++) {
				for (long y = inter.p0().y(); y < inter.p1().y(); y++) {
					bp.set(x, y, z);
					access.setBlockState(bp, block, false);
				}
			}
		}
	}


}
