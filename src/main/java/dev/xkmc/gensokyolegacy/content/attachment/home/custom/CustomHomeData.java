package dev.xkmc.gensokyolegacy.content.attachment.home.custom;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.HomeSearchUtil;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureInfoUpdateToClient;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SerialClass
public class CustomHomeData {

	@SerialField
	private BlockPos rootPos;
	@SerialField
	private BoundingBox pieceBounds;

	@SerialField
	private final List<BlockPos> containers = new ArrayList<>();
	@SerialField
	private final List<BlockPos> chairs = new ArrayList<>();

	public boolean checkInit(CustomHomeHolder holder) {
		if (rootPos == null || pieceBounds == null) {
			var pos = holder.key().pos();
			var radius = 7; // 15x15x15 search radius from StructureWand
			this.rootPos = pos;
			this.pieceBounds = new BoundingBox(
					pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
					pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
			);
		}
		return true;
	}

	public void tick(CustomHomeHolder holder) {
		if (rootPos == null || pieceBounds == null) return;

	}

	public BlockPos getRoot() {
		return rootPos;
	}

	public BoundingBox getRoomBound() {
		return pieceBounds;
	}

	public BoundingBox getHouseBound() {
		return pieceBounds;
	}

	public BoundingBox getTotalBound() {
		return pieceBounds;
	}

	public boolean isOutside(Level level, BlockPos ans) {
		return level.canSeeSky(ans);
	}

	@Nullable
	public BlockPos getContainerAround(CustomHomeHolder holder, BlockPos center, int rxz, int ry, int trail) {
		return HomeSearchUtil.searchBlock(containers, HomeSearchUtil::isValidChest,
				getRoomBound(), holder.level(), center, rxz, ry, trail);
	}

	@Nullable
	public BlockPos getChairAround(CustomHomeHolder holder, BlockPos center, int rxz, int ry, int trail) {
		return HomeSearchUtil.searchBlock(chairs, HomeSearchUtil::isValidChair,
				getRoomBound(), holder.level(), center, rxz, ry, trail);
	}

	public StructureInfoUpdateToClient getAbnormality(StructureKey key) {
		return new StructureInfoUpdateToClient(key, -1, -1, -1);
	}

}
