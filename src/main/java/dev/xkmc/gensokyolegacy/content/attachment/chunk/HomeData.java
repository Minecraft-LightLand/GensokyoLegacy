package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureInfoUpdateToClient;
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
	// For manually created structures (e.g., via StructureWand), store bounds directly
	@SerialField
	private BlockPos rootPos;
	@SerialField
	private BoundingBox pieceBounds;
	@SerialField
	private BoundingBox startBounds;
	private StructureCache.Builder cacheBuilder;
	private IntegrityVerifier verifier;

	@SerialField
	private final List<BlockPos> containers = new ArrayList<>();
	@SerialField
	private final List<BlockPos> chairs = new ArrayList<>();
	@SerialField
	private final AbnormalCache abnormal = new AbnormalCache();
	@SerialField
	private StructureCache cache;

	public boolean checkInit(HomeHolder holder) {
		if (piece == null) {
			// First try to initialize from vanilla structure system
			var structure = holder.level().registryAccess().holderOrThrow(holder.key().getStructure()).value();
			var start = holder.level().structureManager().getStructureWithPieceAt(holder.key().pos(), structure);
			if (start.getStructure() == structure && !start.getPieces().isEmpty()) {
				this.start = start;
				piece = start.getPieces().getFirst();
				return true;
			}

			if (rootPos == null || pieceBounds == null || startBounds == null) {
				var pos = holder.key().pos();
				var radius = 7; // 15x15x15 search radius from StructureWand
				this.rootPos = pos;
				this.pieceBounds = new BoundingBox(
						pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
						pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
				);
				// Start bounds should be larger (inflated by 12 as used in getTotalBound)
				this.startBounds = new BoundingBox(
						pos.getX() - radius - 12, pos.getY() - radius - 12, pos.getZ() - radius - 12,
						pos.getX() + radius + 12, pos.getY() + radius + 12, pos.getZ() + radius + 12
				);
			}
			// For manually created structures, check if bounds are already set
			return true;
		}
		return true;
	}

	public void tick(HomeHolder holder) {
		if (piece == null && (rootPos == null || pieceBounds == null || startBounds == null)) return;
		if (cache == null) {
			if (cacheBuilder == null) {
				cacheBuilder = new StructureCache.Builder(holder.level(), getHouseBound(holder.config()));
			}
			cacheBuilder.tick();
			if (cacheBuilder.isDone()) {
				cache = cacheBuilder.build();
				holder.chunk().setUnsaved(true);
			}
		} else {
			if (verifier == null) {
				verifier = new IntegrityVerifier(holder, getHouseBound(holder.config()),
						getRoomBound(holder.config()), cache, abnormal);
				if (!verifier.isValid()) {
					cache = null;
					cacheBuilder = null;
					verifier = null;
					return;
				}
			}
			if (verifier.tick()) {
				holder.chunk().setUnsaved(true);
			}
		}
	}

	public BlockPos getRoot() {
		if (piece != null) {
			return piece.getLocatorPosition();
		}
		return rootPos;
	}

	public BoundingBox getRoomBound(StructureConfig config) {
		var bound = piece != null ? piece.getBoundingBox() : pieceBounds;
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
		var bound = piece != null ? piece.getBoundingBox() : pieceBounds;
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
		if (start != null) {
			return start.getBoundingBox().inflatedBy(-12);
		}
		return startBounds.inflatedBy(-12);
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

	public List<BlockFix> popFix(int count, FixStage stage) {
		if (verifier == null || !verifier.isValid()) return List.of();
		return verifier.popFix(count, stage);
	}

	public int getBrokenCount() {
		return abnormal.air.size() + abnormal.primary.size() + abnormal.secondary.size();
	}

	public StructureInfoUpdateToClient getAbnormality(StructureKey key) {
		if (cache == null) {
			return new StructureInfoUpdateToClient(key, -1, -1, -1);
		}
		return new StructureInfoUpdateToClient(key,
				abnormal.air.size(), abnormal.primary.size(), abnormal.secondary.size()
		);
	}

	public void init(HomeHolder holder) {
		if (piece != null) return;
		if (rootPos != null) return; // Already initialized manually

		// Try to initialize from vanilla structure system first
		var structure = holder.level().registryAccess().holderOrThrow(holder.key().getStructure()).value();
		var start = holder.level().structureManager().getStructureWithPieceAt(holder.key().pos(), structure);
		if (start.getStructure() == structure && !start.getPieces().isEmpty()) {
			this.start = start;
			piece = start.getPieces().getFirst();
		} else {
			// For manually created structures (e.g., via StructureWand),
			// set up bounds directly
			var pos = holder.key().pos();
			var radius = 7; // 15x15x15 search radius from StructureWand
			this.rootPos = pos;
			this.pieceBounds = new BoundingBox(
					pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
					pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
			);
			// Start bounds should be larger (inflated by 12 as used in getTotalBound)
			this.startBounds = new BoundingBox(
					pos.getX() - radius - 12, pos.getY() - radius - 12, pos.getZ() - radius - 12,
					pos.getX() + radius + 12, pos.getY() + radius + 12, pos.getZ() + radius + 12
			);
		}
	}
}
