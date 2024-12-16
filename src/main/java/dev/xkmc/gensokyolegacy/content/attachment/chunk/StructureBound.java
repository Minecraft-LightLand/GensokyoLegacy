package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class StructureBound {

	private final BoundingBox bound;
	private final int y0, x0, z0, y, x, z, total;

	public StructureBound(BoundingBox bound) {
		this.bound = bound;
		y0 = bound.minY();
		x0 = bound.minX();
		z0 = bound.minZ();
		y = bound.getYSpan();
		x = bound.getXSpan();
		z = bound.getZSpan();
		total = y * x * z;
	}

	public int getSize() {
		return total;
	}

	public void resolve(BlockPos.MutableBlockPos pos, int step) {
		if (step < 0 || step >= total) {
			throw new IllegalArgumentException("invalid step: " + step + " out of " + total);
		}
		int iz = step % z;
		int ix = step / z % x;
		int iy = step / z / x;
		pos.set(ix, iy, iz);
	}

	public int compute(BlockPos pos) {
		if (!bound.isInside(pos)) return -1;
		return ((pos.getY() - y0) * x + pos.getX() - x0) * z + pos.getZ() - z0;
	}

}
