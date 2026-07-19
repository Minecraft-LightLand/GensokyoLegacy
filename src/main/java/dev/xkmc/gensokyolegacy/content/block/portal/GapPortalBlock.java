package dev.xkmc.gensokyolegacy.content.block.portal;

import com.mojang.serialization.MapCodec;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HALF;

public class GapPortalBlock extends BasePortalBlock {

	public static final MapCodec<GapPortalBlock> CODEC = simpleCodec(GapPortalBlock::new);

	public MapCodec<GapPortalBlock> codec() {
		return CODEC;
	}

	public GapPortalBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(HALF, Half.BOTTOM));
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GapPortalBlockEntity(GLBlocks.GAP_BE.get(), pos, state);
	}

	protected BlockState updateShape(BlockState state, Direction dir, BlockState nstate, LevelAccessor level, BlockPos pos, BlockPos npos) {
		var half = state.getValue(HALF);
		if (dir.getAxis() == Direction.Axis.Y && (half == Half.BOTTOM) == (dir == Direction.UP) &&
				!(nstate.is(this) && nstate.getValue(HALF) != half))
			return Blocks.AIR.defaultBlockState();
		return super.updateShape(state, dir, nstate, level, pos, npos);
	}

	@javax.annotation.Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level level = context.getLevel();
		return blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context) ?
				super.getStateForPlacement(context) : null;
	}

	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity user, ItemStack stack) {
		BlockPos blockpos = pos.above();
		level.setBlock(blockpos, defaultBlockState().setValue(HALF, Half.TOP), 3);
		//TODO target pos
	}

	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		if (state.getValue(HALF) == Half.BOTTOM) {
			return super.canSurvive(state, level, pos);
		}
		BlockState blockstate = level.getBlockState(pos.below());
		if (state.getBlock() != this) {
			return super.canSurvive(state, level, pos);
		} else {
			return blockstate.is(this) && blockstate.getValue(HALF) == Half.BOTTOM;
		}
	}

	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) {
			if (player.isCreative()) {
				preventDropFromBottomPart(level, pos, state, player);
			} else {
				dropResources(state, level, pos, null, player, player.getMainHandItem());
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @javax.annotation.Nullable BlockEntity entity, ItemStack stack) {
		super.playerDestroy(level, player, pos, Blocks.AIR.defaultBlockState(), entity, stack);
	}

	protected static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
		var half = state.getValue(HALF);
		if (half == Half.BOTTOM) return;
		BlockPos lo = pos.below();
		BlockState bottom = level.getBlockState(lo);
		if (bottom.is(state.getBlock()) && bottom.getValue(HALF) == Half.BOTTOM) {
			level.setBlock(lo, Blocks.AIR.defaultBlockState(), 35);
			level.levelEvent(player, 2001, lo, Block.getId(bottom));
		}
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}

}
