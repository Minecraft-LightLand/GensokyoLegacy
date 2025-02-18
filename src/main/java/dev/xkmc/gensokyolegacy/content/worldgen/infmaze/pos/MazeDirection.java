package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos;

public enum MazeDirection {
	EAST(MazeAxis.X, 1), WEST(MazeAxis.X, -1),
	UP(MazeAxis.Y, 1), DOWN(MazeAxis.Y, -1),
	SOUTH(MazeAxis.Z, 1), NORTH(MazeAxis.Z, -1);

	public final MazeAxis axis;
	public final int x, y, z, factor;

	MazeDirection(MazeAxis axis, int factor) {
		this.axis = axis;
		this.factor = factor;
		this.x = axis.x * factor;
		this.y = axis.y * factor;
		this.z = axis.z * factor;
	}

	public static MazeDirection getDirection(MazeAxis axis, int factor) {
		return values()[(axis.ordinal() << 1) | ((1 - factor) >> 1)];
	}

}
