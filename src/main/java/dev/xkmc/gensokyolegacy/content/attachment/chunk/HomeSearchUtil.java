package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.ChairEntity;
import dev.xkmc.youkaishomecoming.content.block.furniture.WoodChairBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class HomeSearchUtil {

	public static boolean isValidChest(ServerLevel sl, BlockPos pos) {
		var be = sl.getBlockEntity(pos);
		return be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity ||
				be instanceof CabinetBlockEntity || be instanceof BasketBlockEntity;
	}

	public static boolean isValidChair(ServerLevel sl, BlockPos pos) {
		return sl.getBlockState(pos).getBlock() instanceof WoodChairBlock;
	}

	public static void put(ServerLevel level, BlockPos chest, Function<Boolean, ItemStack> doCraft) {
		if (!isValidChest(level, chest)) return;
		var cap = level.getCapability(Capabilities.ItemHandler.BLOCK, chest, Direction.UP);
		if (cap == null) {
			if (level.getBlockEntity(chest) instanceof BaseContainerBlockEntity cont) {
				cap = new InvWrapper(cont);
			} else return;
		}
		if (ItemHandlerHelper.insertItem(cap, doCraft.apply(true), true).isEmpty()) {
			ItemHandlerHelper.insertItem(cap, doCraft.apply(false), false);
		}
	}

	public static <E extends SmartYoukaiEntity> void setSitting(ServerLevel level, BlockPos pos, E entity) {
		BlockState state = level.getBlockState(pos);
		if (state.getBlock() instanceof WoodChairBlock) {
			List<ChairEntity> seats = level.getEntitiesOfClass(ChairEntity.class, new AABB(pos));
			if (seats.isEmpty()) {
				WoodChairBlock.sitDown(level, pos, entity);
			} else {
				var seat = seats.getFirst();
				var e = seat.getPassengers();
				if (!e.isEmpty() && e.getFirst() instanceof Player) return;
				seat.ejectPassengers();
				entity.startRiding(seat);
			}
		}
	}

	@Nullable
	public static BlockPos searchBlock(List<BlockPos> cache, BiPredicate<ServerLevel, BlockPos> validity, BoundingBox room, ServerLevel sl, BlockPos center, int rxz, int ry, int trail) {
		BoundingBox box = BoundingBox.fromCorners(center.offset(-rxz, -ry, -rxz), center.offset(rxz, ry, rxz));
		int x0 = Math.max(box.minX(), room.minX());
		int y0 = Math.max(box.minY(), room.minY());
		int z0 = Math.max(box.minZ(), room.minZ());
		int x1 = Math.min(box.maxX(), room.maxX());
		int y1 = Math.min(box.maxY(), room.maxY());
		int z1 = Math.min(box.maxZ(), room.maxZ());
		if (x0 > x1 || y0 > y1 || z0 > z1) return null;
		cache.removeIf(e -> sl.isLoaded(e) && !validity.test(sl, e));
		for (var e : cache) {
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
			if (validity.test(sl, pos)) {
				var ans = pos.immutable();
				cache.add(ans);
				return ans;
			}
		}
		return null;
	}

	@Nullable
	public static Vec3 getRandomPos(BoundingBox bound, YoukaiEntity e, Predicate<BlockPos> pred) {
		var rand = e.getRandom();
		return RandomPos.generateRandomPos(e, () -> {
			int x = rand.nextInt(bound.minX(), bound.maxX() + 1);
			int y = rand.nextInt(bound.minY(), bound.maxY() + 1);
			int z = rand.nextInt(bound.minZ(), bound.maxZ() + 1);
			var ans = new BlockPos(x, y, z);
			if (!e.getNavigation().isStableDestination(ans)) return null;
			ans = LandRandomPos.movePosUpOutOfSolid(e, ans);
			if (ans == null || !bound.isInside(ans) || !pred.test(ans)) return null;
			return ans;
		});
	}

}
