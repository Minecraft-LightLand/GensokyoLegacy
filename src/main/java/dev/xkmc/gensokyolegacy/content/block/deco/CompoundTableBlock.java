package dev.xkmc.gensokyolegacy.content.block.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.mult.CreateBlockStateBlockMethod;
import dev.xkmc.l2modularblock.mult.DefaultStateBlockMethod;
import dev.xkmc.l2modularblock.mult.PlacementBlockMethod;
import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.ATTACHED;

public class CompoundTableBlock implements ShapeBlockMethod, CreateBlockStateBlockMethod, DefaultStateBlockMethod, ShapeUpdateBlockMethod, PlacementBlockMethod {

	public static final VoxelShape TOP = Block.box(0, 13, 0, 16, 16, 16);
	public static final VoxelShape CLOTH = Block.box(0, 6, 0, 16, 16, 16);
	public static final VoxelShape STAND = Block.box(6.5, 0, 6.5, 9.5, 13, 9.5);

	public static final VoxelShape BARE = Shapes.or(TOP, STAND);
	public static final VoxelShape CLOTHED = Shapes.or(CLOTH, STAND);

	@Override
	public @Nullable VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(TableClothImpl.COLOR) == TableClothImpl.Color.NONE ?
				state.getValue(ATTACHED) ? TOP : BARE :
				state.getValue(ATTACHED) ? CLOTH : CLOTHED;
	}

	@Override
	public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ATTACHED);
	}

	@Override
	public BlockState getDefaultState(BlockState state) {
		return state.setValue(ATTACHED, false);
	}

	private boolean shouldConnect(LevelAccessor level, BlockPos pos) {
		return level.getBlockState(pos.north()).is(GLTagGen.LARGE_TABLE) &&
				level.getBlockState(pos.south()).is(GLTagGen.LARGE_TABLE) ||
				level.getBlockState(pos.east()).is(GLTagGen.LARGE_TABLE) &&
						level.getBlockState(pos.west()).is(GLTagGen.LARGE_TABLE);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockState def, BlockPlaceContext context) {
		return shouldConnect(context.getLevel(), context.getClickedPos()) ? def.setValue(ATTACHED, true) : def.setValue(ATTACHED, false);
	}

	@Override
	public BlockState updateShape(Block self, BlockState current, BlockState selfOld, Direction from, BlockState sourceState, LevelAccessor level, BlockPos pos, BlockPos sourcePos) {
		return shouldConnect(level, pos) ? current.setValue(ATTACHED, true) : current.setValue(ATTACHED, false);
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_large_table")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var table = pvd.models().getBuilder("block/" + ctx.getName() + "_top")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_large_table_top")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var stall = pvd.models().getBuilder("block/" + ctx.getName() + "_stall")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_large_table_stall")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var builder = pvd.getMultipartBuilder(ctx.get());
		builder.part().modelFile(stall).addModel().condition(ATTACHED, false).end();
		TableClothImpl.buildStates(builder, pvd, table);
	}

	public static void genLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		pvd.add(block, TableClothImpl.loot(pvd, block));
	}

}
