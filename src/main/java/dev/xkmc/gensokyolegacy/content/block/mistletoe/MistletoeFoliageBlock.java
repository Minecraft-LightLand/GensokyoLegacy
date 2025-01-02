package dev.xkmc.gensokyolegacy.content.block.mistletoe;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.RandomTickBlockMethod;
import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.mult.SurviveBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.Nullable;

public class MistletoeFoliageBlock implements
		ShapeBlockMethod, RandomTickBlockMethod, ShapeUpdateBlockMethod,
		PlacementBlockMethod, SurviveBlockMethod {

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

	public static DelegateBlock create() {
		var prop = BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES);
		return DelegateBlock.newBaseBlock(prop, BlockTemplates.ALL_DIRECTION, new MistletoeFoliageBlock(), BlockTemplates.WATER);
	}

	public static boolean isSupported(BlockState self, BlockGetter level, BlockState state, BlockPos pos) {
		return state.isCollisionShapeFullBlock(level, pos);
	}

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(BlockTemplates.FACING).ordinal()];
	}

	@Override
	public BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		return state.setValue(BlockTemplates.FACING, ctx.getClickedFace());
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
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		var next = pos.relative(state.getValue(BlockTemplates.FACING).getOpposite());
		return isSupported(state, level, level.getBlockState(next), next);
	}

	@Override
	public BlockState updateShape(
			Block block, BlockState current, BlockState selfState, Direction dir,
			BlockState neighState, LevelAccessor level, BlockPos selfPos, BlockPos neighPos
	) {
		if (dir.getOpposite() == selfState.getValue(BlockTemplates.FACING)) {
			if (!isSupported(selfState, level, neighState, neighPos)) {
				return Blocks.AIR.defaultBlockState();
			}
		}
		return current;
	}

	public static void buildModels(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		var carpet = pvd.models()
				.carpet(ctx.getName(), pvd.modLoc("block/mistletoe_leaves"))
				.renderType("cutout");
		var foliage = pvd.models().getBuilder(ctx.getName() + "_side")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/mistletoe_side")))
				.texture("all", pvd.modLoc("block/mistletoe_leaves"))
				.renderType("cutout");
		pvd.getVariantBuilder(ctx.get())
				.forAllStates(state -> {
					Direction dir = state.getValue(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(dir.getAxis().isHorizontal() ? foliage : carpet)
							.rotationX(dir == Direction.DOWN ? 180 : 0)
							.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + 180) % 360)
							.build();
				});
	}
}
