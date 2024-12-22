package dev.xkmc.gensokyolegacy.content.block.plant;

import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.RandomTickBlockMethod;
import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

public class MistletoePlaneBlock implements ShapeBlockMethod, RandomTickBlockMethod, ShapeUpdateBlockMethod {

	public static final VoxelShape[] SHAPES;

	static {
		SHAPES = new VoxelShape[6];
		// -1  0  1
		// 15  0  0
		// 16 16  1
		for (Direction dir : Direction.values()) {
			var vec = dir.getNormal();
			SHAPES[dir.ordinal()] = Block.box(
					vec.getX() >= 0 ? 0 : 15,
					vec.getY() >= 0 ? 0 : 15,
					vec.getZ() >= 0 ? 0 : 15,
					vec.getX() > 0 ? 1 : 16,
					vec.getY() > 0 ? 1 : 16,
					vec.getZ() > 0 ? 1 : 16);
		}
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(BlockTemplates.FACING).ordinal()];
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
		var f = state.getValue(BlockTemplates.FACING);
		for (var dir : Direction.values()) {
			if (dir.getAxis() == f.getAxis()) {
				if (dir != f) {
					BlockPos basePos = pos.relative(dir);
					MistletoeLeavesBlock.randomTickPos(pos, state, level, basePos, rand);
				}
				continue;
			}
			BlockPos nextPos = pos.relative(dir);
			BlockState spreadState = level.getBlockState(nextPos);
			if (!spreadState.isAir()) continue;
			BlockPos leavesPos = nextPos.relative(f.getOpposite());
			BlockState leavesState = level.getBlockState(leavesPos);
			if (!MistletoeLeavesBlock.isSpreadable(level, leavesState, leavesPos)) continue;
			if (CommonHooks.canCropGrow(level, pos, state, rand.nextDouble() < MistletoeLeavesBlock.chance())) {
				level.setBlockAndUpdate(nextPos, state);
				CommonHooks.fireCropGrowPost(level, pos, state);
			}
		}
	}

	@Override
	public BlockState updateShape(
			Block block, BlockState current, BlockState selfState, Direction dir,
			BlockState neighState, LevelAccessor level, BlockPos selfPos, BlockPos neighPos
	) {
		if (dir == selfState.getValue(BlockTemplates.FACING)) {
			if (!neighState.is(BlockTags.LEAVES) || !neighState.isCollisionShapeFullBlock(level, neighPos)) {
				return Blocks.AIR.defaultBlockState();
			}
		}
		return current;
	}

	public static DelegateBlock create() {
		var prop = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES);
		return DelegateBlock.newBaseBlock(prop, BlockTemplates.ALL_DIRECTION, new MistletoePlaneBlock(), BlockTemplates.WATER);
	}

}
