package dev.xkmc.gensokyolegacy.content.attachment.home.custom;

public class ClusterBitSet {

	private final boolean[][][] data;
	private final int x0;
	private final int y0;
	private final int z0;
	private final int dx;
	private final int dy;
	private final int dz;

	public ClusterBitSet(RoomData room) {
		x0 = room.bound.minX();
		y0 = room.bound.minY();
		z0 = room.bound.minZ();
		data = new boolean[dx = room.xSize][dy = room.bound.getYSpan()][dz = room.zSize];
		for (int x = 0; x < room.xSize; x++) {
			for (int z = 0; z < room.zSize; z++) {
				for (var arr : room.columns[x][z].list) {
					for (int y = arr[0]; y <= arr[1]; y++) {
						data[x][y - y0][z] = true;
					}
				}
			}
		}
	}

	private boolean data(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= dx || y >= dy || z >= dz) return false;
		return data[x][y][z];
	}

	private boolean edge(boolean edge, boolean b00, boolean b01, boolean b10, boolean b11) {
		if (b00 && b01 && b10 && b11) return false;
		if (b00 && b01 && !b10 && !b11) return !edge;
		if (!b00 && !b01 && b10 && b11) return !edge;
		if (b00 & b10 && !b01 && !b11) return !edge;
		if (!b00 & !b10 && b01 && b11) return !edge;
		return b00 || b01 || b10 || b11;
	}

	public void render(boolean edge, Handle handle) {
		for (int x = 0; x <= dx; x++) {
			for (int y = 0; y <= dy; y++) {
				for (int z = 0; z <= dz; z++) {
					if (x < dx && edge(edge, data(x, y - 1, z - 1), data(x, y, z - 1), data(x, y - 1, z), data(x, y, z))) {
						handle.render(x0 + x, y0 + y, z0 + z, x0 + x + 1, y0 + y, z0 + z);
					}
					if (y < dy && edge(edge, data(x - 1, y, z - 1), data(x, y, z - 1), data(x - 1, y, z), data(x, y, z))) {
						handle.render(x0 + x, y0 + y, z0 + z, x0 + x, y0 + y + 1, z0 + z);
					}
					if (z < dz && edge(edge, data(x - 1, y - 1, z), data(x, y - 1, z), data(x - 1, y, z), data(x, y, z))) {
						handle.render(x0 + x, y0 + y, z0 + z, x0 + x, y0 + y, z0 + z + 1);
					}
				}
			}
		}
	}

	public interface Handle {

		void render(int x0, int y0, int z0, int x1, int y1, int z1);

	}

}
