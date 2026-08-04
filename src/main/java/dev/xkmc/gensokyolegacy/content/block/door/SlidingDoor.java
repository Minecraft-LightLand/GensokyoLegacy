package dev.xkmc.gensokyolegacy.content.block.door;

import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.core.VoxelBuilder;
import dev.xkmc.l2modularblock.impl.DoubleBlockImpl;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import dev.xkmc.l2modularblock.mult.UseWithoutItemBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HALF;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class SlidingDoor implements CreateBlockStateBlockMethod, DefaultStateBlockMethod, PlacementBlockMethod,
		UseWithoutItemBlockMethod, UseItemOnBlockMethod, ShapeBlockMethod {

	public static final int MAX = 4;

	public static final IntegerProperty STACK = IntegerProperty.create("stack", 1, MAX);
	public static final EnumProperty<DoorHingeSide> HINGE = BlockStateProperties.DOOR_HINGE;

	public static final VoxelShape[] SHAPES = new VoxelShape[4];

	static {
		var builder = new VoxelBuilder(0, 0, 7, 16, 16, 9);
		for (int i = 0; i < 4; i++) {
			SHAPES[i] = builder.rotateFromNorth(Direction.from2DDataValue(i));
		}
	}

	public static DelegateBlock create(BlockBehaviour.Properties p) {
		return DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new DoubleBlockImpl(), new SlidingDoor());
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HINGE, STACK);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(HINGE, DoorHingeSide.LEFT).setValue(STACK, 1);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
		if (def == null) return null;
		return def.setValue(HINGE, hingeFromClick(context));
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player pl, BlockHitResult result) {
		if (level.isClientSide()) return InteractionResult.SUCCESS;
		BlockPos bottom = bottom(level, pos);
		BlockState bs = level.getBlockState(bottom);
		if (isLeaf(level, bottom, bs)) {
			open(level, bottom, bs);
		} else {
			close(level, bottom, bs);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player pl, InteractionHand hand, BlockHitResult result) {
		if (stack.is(state.getBlock().asItem())) {
			if (!level.isClientSide() && state.getValue(STACK) < MAX) {
				BlockPos bottom = bottom(level, pos);
				int value = level.getBlockState(bottom).getValue(STACK) + 1;
				setStack(level, bottom, value);
				if (!pl.getAbilities().instabuild) {
					stack.shrink(1);
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPES[state.getValue(HORIZONTAL_FACING).get2DDataValue()];
	}

	private static void open(Level level, BlockPos bottom, BlockState bs) {
		BlockPos pocket = bottom.relative(hingeDir(bs));
		BlockState pocketState = level.getBlockState(pocket);
		int sum = bs.getValue(STACK) + pocketState.getValue(STACK);
		if (sum <= MAX) {
			setStack(level, pocket, sum);
			setAir(level, bottom);
			playSound(level, bottom, true);
		}
	}

	private static void close(Level level, BlockPos bottom, BlockState bs) {
		BlockPos leaf = bottom.relative(hingeDir(bs).getOpposite());
		int move = bs.getValue(STACK) - 1;
		if (move > 0 && level.getBlockState(leaf).isAir()) {
			level.setBlock(leaf, bs.setValue(STACK, move).setValue(HALF, Half.BOTTOM), 3);
			level.setBlock(leaf.above(), bs.setValue(STACK, move).setValue(HALF, Half.TOP), 3);
			setStack(level, bottom, 1);
			playSound(level, bottom, false);
		}
	}

	private static boolean isLeaf(Level level, BlockPos bottom, BlockState bs) {
		BlockState pocket = level.getBlockState(bottom.relative(hingeDir(bs)));
		return pocket.is(bs.getBlock()) && pocket.getValue(HORIZONTAL_FACING) == bs.getValue(HORIZONTAL_FACING);
	}

	private static BlockPos bottom(Level level, BlockPos pos) {
		return level.getBlockState(pos).getValue(HALF) == Half.BOTTOM ? pos : pos.below();
	}

	private static void setStack(Level level, BlockPos bottom, int value) {
		BlockState state = level.getBlockState(bottom);
		level.setBlock(bottom, state.setValue(STACK, value).setValue(HALF, Half.BOTTOM), 3);
		level.setBlock(bottom.above(), state.setValue(STACK, value).setValue(HALF, Half.TOP), 3);
	}

	private static void setAir(Level level, BlockPos bottom) {
		level.setBlock(bottom, Blocks.AIR.defaultBlockState(), 3);
		level.setBlock(bottom.above(), Blocks.AIR.defaultBlockState(), 3);
	}

	private static Direction hingeDir(BlockState state) {
		var facing = state.getValue(HORIZONTAL_FACING);
		return state.getValue(HINGE) == DoorHingeSide.LEFT ? facing.getCounterClockWise() : facing.getClockWise();
	}

	private static void playSound(Level level, BlockPos pos, boolean open) {
		level.playSound(null, pos, open ? SoundEvents.WOODEN_DOOR_OPEN : SoundEvents.WOODEN_DOOR_CLOSE,
				SoundSource.BLOCKS, 1, 1);
	}

	private static DoorHingeSide hingeFromClick(BlockPlaceContext context) {
		Direction left = context.getHorizontalDirection().getCounterClockWise();
		Axis axis = left.getAxis();
		BlockPos cell = context.getClickedPos().relative(context.getClickedFace());
		Vec3 click = context.getClickLocation();
		double coord = axis.choose(click.x, click.y, click.z);
		double center = axis.choose(cell.getX() + 0.5, cell.getY() + 0.5, cell.getZ() + 0.5);
		return coord < center ? DoorHingeSide.LEFT : DoorHingeSide.RIGHT;
	}

}
