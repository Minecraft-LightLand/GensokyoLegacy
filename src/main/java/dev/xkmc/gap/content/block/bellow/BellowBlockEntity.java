package dev.xkmc.gap.content.block.bellow;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

@SerialClass
public class BellowBlockEntity extends BaseBlockEntity implements TickableBlockEntity {

	@SerialField
	private int blowTime;

	public BellowBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		boolean triggered = getBlockState().getValue(BlockStateProperties.TRIGGERED);
		if (!triggered && blowTime > 0) {
			triggered = true;
			blowTime--;
		}
		if (triggered && level != null) {
			var dir = getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
			if (level instanceof ServerLevel sl) {
				var pos = getBlockPos().relative(dir);
				if (sl.getBlockEntity(pos) instanceof AbstractFurnaceBlockEntity be) {
					//TODO accelerate furnace
				}
			}
			blowEntities(level, dir);

		}
	}

	private void blowEntities(Level level, Direction dir) {
		var center = getBlockPos().getCenter().relative(dir, 0.5);
		var hit = level.clip(new ClipContext(center, center.relative(dir, 4), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, CollisionContext.empty()));
		if (hit.getLocation().distanceTo(center) <= 1) return;
		var box = new AABB(center.relative(dir, 0.5), hit.getLocation().relative(dir, -0.5)).inflate(0.5);
		var list = level.getEntities(null, box);
		var flow = new Vec3(dir.step()).scale(0.1);
		for (var e : list) {
			e.push(flow);
			e.hasImpulse = true;
		}
	}

	public boolean isBlowing() {
		return blowTime > 0 || getBlockState().getValue(BlockStateProperties.TRIGGERED);
	}

	public void click() {
		blowTime = 10;
		sync();
	}

}
