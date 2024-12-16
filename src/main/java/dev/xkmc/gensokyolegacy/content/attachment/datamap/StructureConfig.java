package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
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
		@Nullable ResourceLocation outsideBlock,
		@Nullable ResourceLocation primaryFix,
		@Nullable ResourceLocation wouldFix
) {

	public static Builder builder() {
		return new Builder();
	}

	public boolean isOutside(Level level, BlockPos ans) {
		if (level.canSeeSky(ans)) return true;
		if (outsideBlock == null) return false;
		BlockState floor = level.getBlockState(ans.below());
		BlockState on = level.getBlockState(ans);
		TagKey<Block> key = TagKey.create(Registries.BLOCK, outsideBlock);
		return floor.is(key) || on.is(key);
	}

	public boolean isPrimary(BlockState state) {
		return is(primaryFix, state);
	}

	public boolean wouldFix(BlockState state) {
		return is(wouldFix, state);
	}

	private boolean is(@Nullable ResourceLocation tag, BlockState state) {
		if (tag == null) return false;
		TagKey<Block> key = TagKey.create(Registries.BLOCK, tag);
		return state.is(key);
	}

	public static class Builder {

		int xzRoomShrink, topRoomShrink, floorRoomShrink;
		int xzHouseShrink, topHouseShrink, floorHouseShrink;
		@Nullable
		ResourceLocation outSideBlock, primaryFix, wouldFix;

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

		public Builder outside(TagKey<Block> tag) {
			this.outSideBlock = tag.location();
			return this;
		}

		public Builder primary(TagKey<Block> tag) {
			this.primaryFix = tag.location();
			return this;
		}

		public Builder wouldFix(TagKey<Block> tag) {
			this.wouldFix = tag.location();
			return this;
		}


		public void addEntity(EntityType<?> type) {
			entities.add(type);
		}

		public StructureConfig build() {
			return new StructureConfig(entities,
					xzRoomShrink, topRoomShrink, floorRoomShrink,
					xzHouseShrink, topHouseShrink, floorHouseShrink,
					outSideBlock, primaryFix, wouldFix);
		}

	}

}
