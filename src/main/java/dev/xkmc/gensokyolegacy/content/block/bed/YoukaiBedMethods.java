package dev.xkmc.gensokyolegacy.content.block.bed;

import dev.xkmc.l2modularblock.mult.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import javax.annotation.Nullable;

record YoukaiBedMethods() implements FallOnBlockMethod,
		ShapeUpdateBlockMethod, SetPlacedByBlockMethod, CreateBlockStateBlockMethod,
		DefaultStateBlockMethod, PlacementBlockMethod {

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction HORIZONTAL_FACING, BlockState HORIZONTAL_FACINGState, LevelAccessor level, BlockPos currentPos, BlockPos HORIZONTAL_FACINGPos) {
		if (HORIZONTAL_FACING == YoukaiBedBlock.getNeighbourDirection(state.getValue(YoukaiBedBlock.PART), state.getValue(BlockStateProperties.HORIZONTAL_FACING))) {
			return HORIZONTAL_FACINGState.is(block) && HORIZONTAL_FACINGState.getValue(YoukaiBedBlock.PART) != state.getValue(YoukaiBedBlock.PART) ? current : Blocks.AIR.defaultBlockState();
		} else {
			return current;
		}
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(YoukaiBedBlock.PART);
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level.isClientSide) return;
		BlockPos blockpos = pos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING));
		level.setBlock(blockpos, state.setValue(YoukaiBedBlock.PART, BedPart.HEAD), 3);
		level.blockUpdated(pos, Blocks.AIR);
		state.updateNeighbourShapes(level, pos, 3);
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
		Direction direction = context.getHorizontalDirection();
		BlockPos blockpos = context.getClickedPos();
		BlockPos blockpos1 = blockpos.relative(direction);
		Level level = context.getLevel();
		return level.getBlockState(blockpos1).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(blockpos1)
				? state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction)
				: null;
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(YoukaiBedBlock.PART, BedPart.FOOT);
	}

	@Override
	public boolean fallOn(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float v) {
		return true;
	}

}
