package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeWall3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.MazeDirection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

public record LeafType(GateType type, int scale) implements Comparable<LeafType> {

	private static final Comparator<LeafType> COMP = Comparator.comparingInt(LeafType::scale).thenComparingInt(e -> e.type.ordinal());

	@Nullable
	public static LeafType of(MazeCell3D cell) {
		if (cell.pos.scale() > 1) return null;
		GateType gate = cell.getWall(MazeDirection.DOWN).open ? GateType.DOWN :
				cell.getWall(MazeDirection.UP).open ? GateType.UP : GateType.SIDE;
		if (gate == GateType.SIDE && cell.pos.scale() > 0) {
			for (var dir : MazeDirection.values()) {
				var wall = cell.getWall(dir);
				while (wall.open && wall.pos.scale() > 0) {
					long minY = Integer.MAX_VALUE;
					MazeWall3D open = null;
					for (int i = 0; i < 4; i++) {
						var s = wall.loadChild(i);
						minY = Math.min(minY, s.pos.pos().y());
						if (s.open) {
							open = s;
						}
					}
					if (open == null || open.pos.pos().y() > minY) return null;
					wall = open;
				}
			}
		}
		return new LeafType(gate, cell.pos.scale());
	}

	@Override
	public int compareTo(@NotNull LeafType o) {
		return COMP.compare(this, o);
	}
}
