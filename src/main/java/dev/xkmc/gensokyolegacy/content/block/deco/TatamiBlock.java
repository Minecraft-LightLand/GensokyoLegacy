package dev.xkmc.gensokyolegacy.content.block.deco;

import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TatamiBlock implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
		ShapeUpdateBlockMethod, SetPlacedByBlockMethod, SurviveBlockMethod, PlayerDestoryBlockMethod, ShapeBlockMethod {

	public enum Kind implements StringRepresentable {
		FRONT, END, SQUARE;

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}

		public Kind opposite() {
			return this == FRONT ? END : this == END ? FRONT : SQUARE;
		}
	}

	public static final EnumProperty<Kind> KIND = EnumProperty.create("kind", Kind.class, List.of(Kind.values()));

	public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 1, 16);

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	public BlockState updateShape(Block self, BlockState current, BlockState state, Direction dir, BlockState nstate, LevelAccessor level, BlockPos pos, BlockPos npos) {
		if (dir.getAxis() == Direction.Axis.Y) {
			if (!canSurvive(current, level, pos)) return Blocks.AIR.defaultBlockState();
		}
		var half = current.getValue(KIND);
		var facing = current.getValue(HORIZONTAL_FACING);
		if (facing == dir && (!nstate.is(self) || nstate.getValue(HORIZONTAL_FACING) != facing.getOpposite() || nstate.getValue(KIND) != half.opposite()))
			return Blocks.AIR.defaultBlockState();

		return current;
	}

	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(KIND);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(KIND, Kind.SQUARE);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState state, BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		var dir = state.getValue(HORIZONTAL_FACING).getOpposite();
		state = state.setValue(HORIZONTAL_FACING, dir);
		return level.getBlockState(blockpos.relative(dir)).canBeReplaced(context) ? state.setValue(KIND, Kind.FRONT) : state;
	}

	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity user, ItemStack stack) {
		if (state.getValue(KIND) == Kind.SQUARE) return;
		var dir = state.getValue(HORIZONTAL_FACING);
		BlockPos blockpos = pos.relative(dir);
		level.setBlock(blockpos, state.setValue(KIND, Kind.END).setValue(HORIZONTAL_FACING, dir.getOpposite()), 3);
	}

	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (level.isEmptyBlock(pos.below())) return false;
		if (state.getValue(KIND) != Kind.END) return true;
		BlockState other = level.getBlockState(pos.relative(state.getValue(HORIZONTAL_FACING)));
		return other.is(state.getBlock()) && other.getValue(KIND) == state.getValue(KIND).opposite();
	}

	public @Nullable BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) {
			if (player.isCreative()) {
				preventDropFromBottomPart(level, pos, state, player);
			} else {
				Block.dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}
		return null;
	}

	public boolean playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity entity, ItemStack stack) {
		return true;
	}

	public static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		var half = state.getValue(KIND);
		if (half == Kind.SQUARE || half == Kind.END) return;
		BlockPos lo = pos.relative(state.getValue(HORIZONTAL_FACING));
		BlockState bottom = level.getBlockState(lo);
		if (bottom.is(state.getBlock()) && bottom.getValue(KIND) == Kind.END) {
			level.setBlock(lo, Blocks.AIR.defaultBlockState(), 35);
			level.levelEvent(player, 2001, lo, Block.getId(bottom));
		}
	}

}
