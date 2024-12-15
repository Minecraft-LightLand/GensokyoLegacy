package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;

public record StructureConfig(
		LinkedHashSet<EntityType<?>> entities,
		int xzRoomShrink, int topRoomShrink, int floorRoomShrink,
		int xzHouseShrink, int topHouseShrink, int floorHouseShrink,
		@Nullable HolderSet<Block> outsideBlock,
		@Nullable HolderSet<Block> primaryFix
) {

	public static Builder builder() {
		return new Builder();
	}

	public boolean isOutside(Level level, BlockPos ans) {
		if (level.canSeeSky(ans)) return true;
		if (outsideBlock == null) return false;
		BlockState floor = level.getBlockState(ans.below());
		BlockState on = level.getBlockState(ans);
		return floor.is(outsideBlock) || on.is(outsideBlock);
	}

	public static class Builder {

		int xzRoomShrink, topRoomShrink, floorRoomShrink;
		int xzHouseShrink, topHouseShrink, floorHouseShrink;
		@Nullable
		HolderSet<Block> outSideBlock, primaryFix;

		LinkedHashSet<EntityType<?>> entities = new LinkedHashSet<>();

		public Builder room(int xz, int top, int floor) {
			this.xzRoomShrink = xz;
			this.topRoomShrink = top;
			this.floorRoomShrink = floor;
			return this;
		}

		public Builder house(int xz, int top, int floor) {
			this.xzHouseShrink = xz;
			this.topHouseShrink = top;
			this.floorHouseShrink = floor;
			return this;
		}

		public Builder outside(HolderSet<Block> tag) {
			this.outSideBlock = tag;
			return this;
		}

		public Builder primary(HolderSet<Block> tag) {
			this.primaryFix = tag;
			return this;
		}

		public void addEntity(EntityType<?> type) {
			entities.add(type);
		}

		public StructureConfig build() {
			return new StructureConfig(entities,
					xzRoomShrink, topRoomShrink, floorRoomShrink,
					xzHouseShrink, topHouseShrink, floorHouseShrink,
					outSideBlock, primaryFix);
		}

	}

}
