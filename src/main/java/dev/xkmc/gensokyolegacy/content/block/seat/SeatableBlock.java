package dev.xkmc.gensokyolegacy.content.block.seat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class SeatableBlock extends Block {

	private final float offset;

	public SeatableBlock(Properties pProperties, float offset) {
		super(pProperties);
		this.offset = offset;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (player.isShiftKeyDown()) {
			return InteractionResult.PASS;
		}
		List<ChairEntity> seats = level.getEntitiesOfClass(ChairEntity.class, new AABB(pos));
		if (!seats.isEmpty()) {
			ChairEntity seat = seats.getFirst();
			List<Entity> passengers = seat.getPassengers();
			if (!passengers.isEmpty() && passengers.getFirst() instanceof Player) {
				return InteractionResult.PASS;
			}
			if (!level.isClientSide) {
				seat.ejectPassengers();
				player.startRiding(seat);
			}
			return InteractionResult.SUCCESS;
		}
		if (!level.isClientSide) {
			sitDown(level, pos, player);
		}
		return InteractionResult.SUCCESS;
	}

	public void sitDown(Level world, BlockPos pos, Entity entity) {
		if (!world.isClientSide) {
			ChairEntity seat = new ChairEntity(world, pos);
			seat.setPos(pos.getX() + 0.5F, pos.getY() + offset, pos.getZ() + 0.5F);
			world.addFreshEntity(seat);
			entity.startRiding(seat, true);
		}
	}

	public static boolean isSeatOccupied(Level world, BlockPos pos) {
		return !world.getEntitiesOfClass(ChairEntity.class, new AABB(pos)).isEmpty();
	}

}
