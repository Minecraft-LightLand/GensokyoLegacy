package dev.xkmc.gensokyolegacy.content.block.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.l2core.serial.loot.LootHelper;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.*;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

public class BooksBlock implements ShapeBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod, UseWithoutItemBlockMethod, PlacementBlockMethod, SurviveBlockMethod {

	private static final int MAX = 2;
	public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, MAX);
	public static final VoxelShape[] SHAPES = {
			Block.box(0, 0, 0, 16, 5, 16),
			Block.box(0, 0, 0, 16, 1, 16),
			Block.box(0, 0, 0, 16, 3, 16)
	};

	@Nullable
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		int variant = state.getValue(VARIANT);
		return SHAPES[variant];
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(VARIANT);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(VARIANT, 0);
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return Block.canSupportCenter(level, pos.below(), Direction.UP);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide()) {
			level.setBlockAndUpdate(pos, state.setValue(VARIANT, (state.getValue(VARIANT) + 1) % (MAX + 1)));
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState state, BlockPlaceContext ctx) {
		if (!canSurvive(state, ctx.getLevel(), ctx.getClickedPos()))
			return null;
		return state.setValue(VARIANT, ctx.getLevel().random.nextInt(0, MAX + 1));
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), state -> {
			int variant = state.getValue(VARIANT);
			String model = ctx.getName() + "_" + variant;
			String id = variant > 0 ? model : ctx.getName();
			return pvd.models().getBuilder("block/" + id)
					.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + model)))
					.texture("books", pvd.modLoc("block/books"));
		});
	}

	public static void buildLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		var helper = new LootHelper(pvd);
		pvd.add(block, LootTable.lootTable().withPool(
				pvd.applyExplosionCondition(Items.BOOK, LootPool.lootPool().add(helper.item(Items.BOOK, 5)))));
	}
}
