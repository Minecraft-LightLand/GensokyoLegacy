package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class HomeData {

	private StructureStart start;
	private StructurePiece piece;

	@SerialField
	private final List<BlockPos> containers = new ArrayList<>();

	@SerialField
	private final List<BlockPos> chairs = new ArrayList<>();

	public boolean checkInit(HomeHolder holder) {
		if (piece == null) {
			var structure = holder.level().registryAccess().holderOrThrow(holder.key().getStructure()).value();
			var start = holder.level().structureManager().getStructureWithPieceAt(holder.key().pos(), structure);
			if (start.getStructure() != structure) return false;
			if (start.getPieces().isEmpty()) return false;
			this.start = start;
			piece = start.getPieces().getFirst();
		}
		return true;
	}

	public BlockPos getRoot() {
		return piece.getLocatorPosition();
	}

	public BoundingBox getRoomBound(StructureConfig config) {
		var bound = piece.getBoundingBox();
		return new BoundingBox(
				bound.minX() + config.xzRoomShrink(),
				bound.minY() + config.floorRoomShrink(),
				bound.minZ() + config.xzRoomShrink(),
				bound.maxX() - config.xzRoomShrink(),
				bound.maxY() - config.topRoomShrink(),
				bound.maxZ() - config.xzRoomShrink()
		);
	}

	public BoundingBox getHouseBound(StructureConfig config) {
		var bound = piece.getBoundingBox();
		return new BoundingBox(
				bound.minX() + config.xzHouseShrink(),
				bound.minY() + config.floorHouseShrink(),
				bound.minZ() + config.xzHouseShrink(),
				bound.maxX() - config.xzHouseShrink(),
				bound.maxY() - config.topHouseShrink(),
				bound.maxZ() - config.xzHouseShrink()
		);
	}

	public BoundingBox getTotalBound() {
		return start.getBoundingBox();
	}

	@Nullable
	public BlockPos getContainerAround(HomeHolder holder, BlockPos center, int rxz, int ry, int trail) {
		return HomeSearchUtil.searchBlock(containers, HomeSearchUtil::isValidChest,
				getRoomBound(holder.config()), holder.level(), center, rxz, ry, trail);
	}

	@Nullable
	public BlockPos getChairAround(HomeHolder holder, BlockPos center, int rxz, int ry, int trail) {
		return HomeSearchUtil.searchBlock(chairs, HomeSearchUtil::isValidChair,
				getRoomBound(holder.config()), holder.level(), center, rxz, ry, trail);
	}

	private void verifyStructureIntegrity() {

	}
}
