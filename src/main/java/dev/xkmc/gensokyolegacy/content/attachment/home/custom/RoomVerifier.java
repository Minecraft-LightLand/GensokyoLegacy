package dev.xkmc.gensokyolegacy.content.attachment.home.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class RoomVerifier {

	private static final AABB UNIT = new AABB(0, 0, 0, 1, 1, 1);
	private static final AABB WALK = new AABB(3 / 16d, 3 / 16d, 3 / 16d, 13 / 16d, 13 / 16d, 13 / 16d);
	private static final int MAX_HEIGHT = 6;

	public enum VisitResult {
		NONE, SUCCEED, REPEAT, BLOCKED, TOO_THIN, NO_FLOOR, NO_ROOF;

		public boolean error() {
			return this == NO_ROOF || this == NO_FLOOR;
		}

	}

	public record VisitFeedback(VisitResult res, BlockPos pos) {
	}

	private static class BlockData {

		private final BlockPos pos;
		private final BlockState state;
		private int toRoof, toFloor, cull;
		private boolean solid, door;

		private BlockData(BlockPos pos, BlockState state) {
			this.pos = pos;
			this.state = state;
		}

		public boolean culling(Direction dir) {
			return (cull & (1 << dir.get3DDataValue())) != 0;
		}

		public void updateShape(AABB bounds) {
			if (bounds.intersects(WALK)) {
				cull = 0x3f;
				solid = true;
				return;
			}
			boolean xs = bounds.minX == 0 && bounds.maxX == 1;
			boolean ys = bounds.minY == 0 && bounds.maxY == 1;
			boolean zs = bounds.minZ == 0 && bounds.maxZ == 1;
			if (xs && zs) {
				if (bounds.minY == 0) cull |= 1 << Direction.DOWN.get3DDataValue();
				if (bounds.maxY == 1) cull |= 1 << Direction.UP.get3DDataValue();
			}
			if (xs && ys) {
				if (bounds.minZ == 0) cull |= 1 << Direction.NORTH.get3DDataValue();
				if (bounds.maxZ == 1) cull |= 1 << Direction.SOUTH.get3DDataValue();
			}
			if (zs && ys) {
				if (bounds.minX == 0) cull |= 1 << Direction.WEST.get3DDataValue();
				if (bounds.maxX == 1) cull |= 1 << Direction.EAST.get3DDataValue();
			}
		}
	}

	private final ServerLevel level;
	private final ServerPlayer player;
	private final Queue<BlockData[]> queue = new ArrayDeque<>();
	private final Set<BlockPos> visited = new LinkedHashSet<>();
	private final Set<BlockData[]> valid = new LinkedHashSet<>();
	private final Map<BlockPos, BlockData> data = new LinkedHashMap<>();

	private int x0, y0, z0, x1, y1, z1, size = 0;

	public RoomVerifier(ServerLevel sl, ServerPlayer player) {
		this.level = sl;
		this.player = player;
	}

	public VisitResult visit(BlockPos pos) {
		if (visited.contains(pos)) return VisitResult.REPEAT;
		var val = of(pos);
		visited.add(pos);
		if (val.solid) return VisitResult.BLOCKED;
		int n = MAX_HEIGHT;
		BlockData[] toFloor = new BlockData[n];
		toFloor[0] = val;
		int i = 0;
		while (true) {
			if (toFloor[i].culling(Direction.DOWN)) break;
			if (i >= n - 1) return VisitResult.NO_FLOOR;
			var ipos = toFloor[i].pos.below();
			toFloor[i + 1] = of(ipos);
			if (toFloor[i + 1].solid || toFloor[i + 1].culling(Direction.UP)) break;
			if (visited.contains(ipos)) return VisitResult.REPEAT;
			visited.add(ipos);
			i++;
		}
		int floor = i;
		toFloor[i].toFloor = 1;
		for (i--; i >= 0; i--) {
			toFloor[i].toFloor = toFloor[i + 1].toFloor + 1;
		}

		BlockData[] toRoof = new BlockData[n];
		toRoof[0] = val;
		i = 0;
		while (true) {
			if (toRoof[i].culling(Direction.UP)) break;
			if (i >= n - 1) return VisitResult.NO_ROOF;
			var ipos = toRoof[i].pos.above();
			toRoof[i + 1] = of(ipos);
			if (toRoof[i + 1].solid || toRoof[i + 1].culling(Direction.DOWN)) break;
			i++;
		}
		int roof = i;
		if (floor + roof == 0) return VisitResult.TOO_THIN;
		if (floor + roof >= n) return VisitResult.NO_ROOF;
		toRoof[i].toRoof = 1;
		for (i--; i >= 0; i--) {
			toRoof[i].toRoof = toRoof[i + 1].toRoof + 1;
		}
		BlockData[] col = new BlockData[floor + roof + 1];
		col[floor] = val;
		for (i = 1; i <= floor; i++) {
			toFloor[i].toRoof = toFloor[i - 1].toRoof + 1;
			col[floor - i] = toFloor[i];
		}
		for (i = 1; i <= roof; i++) {
			toRoof[i].toFloor = toRoof[i - 1].toFloor + 1;
			col[floor + i] = toRoof[i];
		}

		queue.add(col);
		valid.add(col);
		var p0 = col[0].pos;
		x0 = Math.min(x0, p0.getX());
		x1 = Math.max(x1, p0.getX());
		z0 = Math.min(z0, p0.getZ());
		z1 = Math.max(z1, p0.getZ());
		y0 = Math.min(y0, p0.getY());
		y1 = Math.max(y1, p0.getY() + col[0].toRoof - 1);
		size += col[0].toRoof;
		return VisitResult.SUCCEED;
	}

	private BlockData of(BlockPos pos) {
		if (data.containsKey(pos)) return data.get(pos);
		var state = level.getBlockState(pos);
		var ans = new BlockData(pos, state);
		if (state.getBlock() instanceof DoorBlock) {
			ans.door = true;
			ans.solid = true;
		} else {
			var shape = state.getCollisionShape(level, pos);
			if (!shape.isEmpty()) {
				ans.updateShape(shape.bounds().intersect(UNIT));
			}
		}
		data.put(pos, ans);
		return ans;
	}

	public VisitFeedback step() {
		if (queue.isEmpty()) return new VisitFeedback(VisitResult.NONE, BlockPos.ZERO);
		var prev = queue.poll();
		for (var dir : Direction.values()) {
			if (dir.getAxis() == Direction.Axis.Y) continue;
			int wall = 0;
			for (BlockData dat : prev) {
				if (dat.culling(dir)) {
					wall = 0;
					continue;
				}
				wall++;
				if (wall > 1) {
					var pos = dat.pos.relative(dir);
					var ret = visit(pos);
					if (ret.error()) return new VisitFeedback(ret, pos);
				}
			}
		}
		return new VisitFeedback(VisitResult.SUCCEED, BlockPos.ZERO);
	}

	@Nullable
	public BoundingBox run(BlockPos pos) {
		x0 = x1 = pos.getX();
		y0 = y1 = pos.getY();
		z0 = z1 = pos.getZ();
		var ini = visit(pos);
		if (ini != VisitResult.SUCCEED) {
			player.sendSystemMessage(Component.literal("Initial column failed: " + ini.name()));
		}
		while (!queue.isEmpty()) {
			var ans = step();
			if (ans.res().error()) {
				var p = ans.pos;
				player.sendSystemMessage(Component.literal("FATAL: %s column at (%d, %d, %d)".formatted(ans.res.name(), p.getX(), p.getY(), p.getZ())));
				return null;
			}
		}
		player.sendSystemMessage(Component.literal("Total columns: %d".formatted(valid.size())));
		player.sendSystemMessage(Component.literal("Bound: (%d, %d, %d) ~ (%d, %d, %d))".formatted(x0, y0, z0, x1, y1, z1)));
		player.sendSystemMessage(Component.literal("Total size: %d".formatted(size)));
		return new BoundingBox(x0, y0, z0, x1, y1, z1);
	}

}
