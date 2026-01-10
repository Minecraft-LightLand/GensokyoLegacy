package dev.xkmc.gensokyolegacy.content.block.bed;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.phys.Vec3;

public class YoukaiBedBlock extends DelegateEntityBlockImpl {

	public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GLBlocks.BE_BED, YoukaiBedBlockEntity.class);

	public YoukaiBedBlock(BlockBehaviour.Properties properties) {
		super(properties, BlockTemplates.HORIZONTAL, new YoukaiBedMethods(), TE);
	}

	public static Direction getNeighbourDirection(BedPart part, Direction direction) {
		return part == BedPart.FOOT ? direction : direction.getOpposite();
	}

	public static Direction getConnectedDirection(BlockState state) {
		Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
		return state.getValue(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
	}

	public static void buildLoot(
			RegistrateBlockLootTables pvd, Block block
	) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(pvd.applyExplosionCondition(block, LootPool.lootPool()
				.add(LootItem.lootTableItem(block).when(helper.enumState(block, PART, BedPart.HEAD))))));
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(level, entity);
		} else {
			Vec3 vec3 = entity.getDeltaMovement();
			if (vec3.y < 0) {
				double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
				entity.setDeltaMovement(vec3.x, -vec3.y * 0.66F * d0, vec3.z);
			}
		}
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide && player.isCreative()) {
			BedPart bedpart = state.getValue(PART);
			if (bedpart == BedPart.FOOT) {
				BlockPos blockpos = pos.relative(getNeighbourDirection(bedpart, state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
				BlockState blockstate = level.getBlockState(blockpos);
				if (blockstate.is(this) && blockstate.getValue(PART) == BedPart.HEAD) {
					level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 35);
					level.levelEvent(player, 2001, blockpos, Block.getId(blockstate));
				}
			}
		}
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	protected long getSeed(BlockState state, BlockPos pos) {
		BlockPos blockpos = pos.relative(state.getValue(BlockStateProperties.HORIZONTAL_FACING), state.getValue(PART) == BedPart.HEAD ? 0 : 1);
		return Mth.getSeed(blockpos.getX(), pos.getY(), blockpos.getZ());
	}

	@Override
	public boolean isBed(BlockState state, BlockGetter level, BlockPos pos, LivingEntity sleeper) {
		return true;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	@Override
	public void setBedOccupied(BlockState state, Level level, BlockPos pos, LivingEntity sleeper, boolean occupied) {

	}
}

