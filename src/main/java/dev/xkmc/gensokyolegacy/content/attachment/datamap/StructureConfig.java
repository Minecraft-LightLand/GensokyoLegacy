package dev.xkmc.gensokyolegacy.content.attachment.datamap;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

public record StructureConfig(
		LinkedHashSet<EntityType<?>> entities,
		int xzRoomShrink, int topRoomShrink, int floorRoomShrink,
		int xzHouseShrink, int topHouseShrink, int floorHouseShrink,
		@Nullable HolderSet<Block> outsideBlock,
		@Nullable HolderSet<Block> primaryFix,
		@Nullable HolderSet<Block> wouldFix
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

	public boolean isPrimary(BlockState state) {
		if (primaryFix == null) return false;
		return state.is(primaryFix);
	}

	public boolean wouldFix(BlockState state) {
		if (wouldFix == null) return false;
		return state.is(wouldFix);
	}

	public static class Builder {

		int xzRoomShrink, topRoomShrink, floorRoomShrink;
		int xzHouseShrink, topHouseShrink, floorHouseShrink;
		@Nullable
		HolderSet<Block> outSideBlock, primaryFix, wouldFix;

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
			this.outSideBlock = new FakeHolderSet<>(tag);
			return this;
		}

		public Builder primary(TagKey<Block> tag) {
			this.primaryFix = new FakeHolderSet<>(tag);
			return this;
		}

		public Builder wouldFix(TagKey<Block> tag) {
			this.wouldFix = new FakeHolderSet<>(tag);
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

	private static class FakeHolderSet<T> extends HolderSet.ListBacked<T> {
		private final TagKey<T> key;

		FakeHolderSet(TagKey<T> key) {
			this.key = key;
		}

		public TagKey<T> key() {
			return this.key;
		}

		protected List<Holder<T>> contents() {
			return List.of();
		}

		public Either<TagKey<T>, List<Holder<T>>> unwrap() {
			return Either.left(this.key);
		}

		public Optional<TagKey<T>> unwrapKey() {
			return Optional.of(this.key);
		}

		public boolean contains(Holder<T> holder) {
			return holder.is(this.key);
		}

		public String toString() {
			return "NamedSet(" + key + ")";
		}

	}

}
