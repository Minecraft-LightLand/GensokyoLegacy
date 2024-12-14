package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class HomeData {

	private StructurePiece piece;

	@SerialField
	private final List<BlockPos> containers = new ArrayList<>();

	public boolean checkInit(HomeHolder holder) {
		if (piece == null) {
			var structure = holder.level().registryAccess().holderOrThrow(holder.key().getStructure()).value();
			var start = holder.level().structureManager().getStructureWithPieceAt(holder.key().pos(), structure);
			if (start.getStructure() != structure) return false;
			if (start.getPieces().isEmpty()) return false;
			piece = start.getPieces().getFirst();
		}
		return true;
	}

	public BlockPos getRoot() {
		return piece.getLocatorPosition();
	}

	public BoundingBox getBound() {
		return piece.getBoundingBox();
	}

	@Nullable
	public BlockPos getContainerAround(ServerLevel sl, BlockPos center, int rxz, int ry, int trail) {
		BoundingBox box = BoundingBox.fromCorners(center.offset(-rxz, -ry, -rxz), center.offset(rxz, ry, rxz));
		BoundingBox room = piece.getBoundingBox();
		int x0 = Math.max(box.minX(), room.minX());
		int y0 = Math.max(box.minY(), room.minY());
		int z0 = Math.max(box.minZ(), room.minZ());
		int x1 = Math.min(box.maxX(), room.maxX());
		int y1 = Math.min(box.maxY(), room.maxY());
		int z1 = Math.min(box.maxZ(), room.maxZ());
		if (x0 > x1 || y0 > y1 || z0 > z1) return null;
		containers.removeIf(e -> sl.isLoaded(e) && !HomeChestUtil.isValid(sl.getBlockEntity(e)));
		for (var e : containers) {
			if (!sl.isLoaded(e)) continue;
			if (box.isInside(e)) {
				return e;
			}
		}
		var rand = sl.getRandom();
		var pos = new BlockPos.MutableBlockPos();
		for (int i = 0; i < trail; i++) {
			int x = rand.nextInt(x0, x1 + 1);
			int y = rand.nextInt(y0, y1 + 1);
			int z = rand.nextInt(z0, z1 + 1);
			pos.set(x, y, z);
			if (HomeChestUtil.isValid(sl.getBlockEntity(pos))) {
				var ans = pos.immutable();
				containers.add(ans);
				return ans;
			}
		}
		return null;
	}

}
