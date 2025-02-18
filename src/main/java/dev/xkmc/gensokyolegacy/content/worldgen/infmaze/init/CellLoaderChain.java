package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.BasePos;

public class CellLoaderChain {

	private final BasePos rootPos;
	private final MazeCell3D root;

	private final MazeCell3D[] list;
	private final int end;
	private final BasePos pos;

	public CellLoaderChain(BasePos rootPos, MazeCell3D root, BasePos pos) {
		this.rootPos = rootPos;
		this.root = root;
		end = root.pos.scale();
		this.pos = pos;
		list = new MazeCell3D[end + 1];
		list[end] = root;
	}

	public boolean sameRoot(BasePos raw) {
		return rootPos.equals(raw);
	}

	public CellLoaderChain of(BasePos pos) {
		return new CellLoaderChain(rootPos, root, pos);
	}

	public MazeCell3D load() {
		for (int s = end; s >= 0; s--) {
			if (list[s] == null) {
				int index = locate(pos, s);
				list[s] = list[s + 1].loadChild(index);
			}
			if (list[s].isLeaf()) {
				return list[s];
			}
		}
		return list[0];
	}

	private static int locate(BasePos pos, int scale) {
		long x = (pos.x() >> scale) & 1;
		long y = (pos.y() >> scale) & 1;
		long z = (pos.z() >> scale) & 1;
		return (int) (x | y << 1 | z << 2);
	}

}
