package dev.xkmc.gensokyolegacy.content.block;

import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

record YoukaiBedMethods() implements FallOnBlockMethod,
		ShapeUpdateBlockMethod, SetPlacedByBlockMethod, CreateBlockStateBlockMethod,
		DefaultStateBlockMethod, PlacementBlockMethod, ShapeBlockMethod {

	private static final int HEIGHT = 9;
	private static final int LEG_WIDTH = 3;
	private static final VoxelShape BASE = Block.box(0, 3, 0, 16, HEIGHT, 16);
	private static final VoxelShape LEG_NORTH_WEST = Block.box(0, 0, 0, LEG_WIDTH, LEG_WIDTH, LEG_WIDTH);
	private static final VoxelShape LEG_SOUTH_WEST = Block.box(0, 0, 16 - LEG_WIDTH, LEG_WIDTH, LEG_WIDTH, 16);
	private static final VoxelShape LEG_NORTH_EAST = Block.box(16 - LEG_WIDTH, 0, 0, 16, LEG_WIDTH, LEG_WIDTH);
	private static final VoxelShape LEG_SOUTH_EAST = Block.box(16 - LEG_WIDTH, 0, 16 - LEG_WIDTH, 16, LEG_WIDTH, 16);
	private static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_NORTH_EAST);
	private static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);
	private static final VoxelShape WEST_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_SOUTH_WEST);
	private static final VoxelShape EAST_SHAPE = Shapes.or(BASE, LEG_NORTH_EAST, LEG_SOUTH_EAST);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (YoukaiBedBlock.getConnectedDirection(state).getOpposite()) {
			case NORTH -> NORTH_SHAPE;
			case SOUTH -> SOUTH_SHAPE;
			case WEST -> WEST_SHAPE;
			default -> EAST_SHAPE;
		};
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
		if (facing == YoukaiBedBlock.getNeighbourDirection(state.getValue(YoukaiBedBlock.PART), state.getValue(BlockStateProperties.FACING))) {
			return facingState.is(block) && facingState.getValue(YoukaiBedBlock.PART) != state.getValue(YoukaiBedBlock.PART) ? current : Blocks.AIR.defaultBlockState();
		} else {
			return current;
		}
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, YoukaiBedBlock.PART);
	}

	/**
	 * Called by BlockItem after this block has been placed.
	 */
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (level.isClientSide) return;
		BlockPos blockpos = pos.relative(state.getValue(BlockStateProperties.FACING));
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
				? state.setValue(BlockStateProperties.FACING, direction)
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
