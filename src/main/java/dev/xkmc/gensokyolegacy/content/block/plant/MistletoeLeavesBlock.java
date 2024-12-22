package dev.xkmc.gensokyolegacy.content.block.plant;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;

public class MistletoeLeavesBlock extends LeavesBlock {

	public static double chance() {
		return 1;
	}

	public static boolean isMistletoe(BlockState state) {
		return state.is(GLBlocks.MISTLETOE_LEAVES) || state.is(GLBlocks.MISTLETOE_FOLIAGE);
	}

	public static boolean isSpreadable(Level level, BlockState state, BlockPos pos) {
		return state.is(BlockTags.LEAVES) && !isMistletoe(state) && state.isCollisionShapeFullBlock(level, pos);
	}

	public static void randomTickPos(BlockPos origin, BlockState originState, ServerLevel level, BlockPos pos, RandomSource rand) {
		BlockState state = level.getBlockState(pos);
		if (!isSpreadable(level, state, pos)) return;
		int count = 0;
		for (var dir : Direction.values()) {
			BlockState neigh = level.getBlockState(pos.relative(dir));
			if (isMistletoe(neigh)) {
				count++;
			}
		}
		if (count < 3) return;
		if (CommonHooks.canCropGrow(level, origin, originState, rand.nextDouble() < MistletoeLeavesBlock.chance())) {
			level.setBlockAndUpdate(pos, state);
			CommonHooks.fireCropGrowPost(level, origin, originState);
		}
	}

	public MistletoeLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(PERSISTENT)) return;
		for (var dir : Direction.values()) {
			randomTickPos(pos, state, level, pos.relative(dir), random);
		}
	}

	public static void buildLeavesLoot(RegistrateBlockLootTables pvd, MistletoeLeavesBlock block) {
		var helper = new LootHelper(pvd);
		var cond = helper.silk().or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)));
		var leaves = LootItem.lootTableItem(block).when(cond);
		var fruits = LootItem.lootTableItem(GLItems.MISTLETOE_BRANCH)
				.when(helper.fortuneChance(10, 8, 6, 5));
		var drops = AlternativesEntry.alternatives(leaves, fruits);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

	public static void buildFoliageLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		var cond = helper.silk().or(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)));
		var leaves = LootItem.lootTableItem(block).when(cond);
		var fruits = LootItem.lootTableItem(GLItems.MISTLETOE_BRANCH)
				.when(helper.fortuneChance(50, 45, 40, 35));
		var drops = AlternativesEntry.alternatives(leaves, fruits);
		pvd.add(block, LootTable.lootTable().withPool(LootPool.lootPool().add(drops)
				.when(ExplosionCondition.survivesExplosion())));
	}

}
