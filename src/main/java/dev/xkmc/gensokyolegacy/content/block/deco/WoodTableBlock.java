package dev.xkmc.gensokyolegacy.content.block.deco;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLDecoBlocks;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class WoodTableBlock implements ShapeBlockMethod {

	public static final VoxelShape SHAPE = Shapes.or(
			Block.box(0, 14, 0, 16, 16, 16),
			Block.box(5, 13, 5, 11, 14, 11),
			Block.box(7, 4, 7, 9, 13, 9),
			Block.box(6, 2, 6, 10, 4, 10),
			Block.box(4, 0, 4, 12, 2, 12)
	);

	public static final VoxelShape CLOTH = Shapes.or(
			Block.box(0, 6, 0, 16, 16, 16),
			Block.box(7, 4, 7, 9, 13, 9),
			Block.box(6, 2, 6, 10, 4, 10),
			Block.box(4, 0, 4, 12, 2, 12)
	);

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return state.getValue(TableClothImpl.COLOR) == TableClothImpl.Color.NONE ? SHAPE : CLOTH;
	}

	public static void buildStates(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.models().getBuilder("block/" + ctx.getName())
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_dining_table")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var table = pvd.models().getBuilder("block/" + ctx.getName() + "_top")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_dining_table_top")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var stall = pvd.models().getBuilder("block/" + ctx.getName() + "_stall")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wooden_dining_table_stall")))
				.texture("all", pvd.modLoc("block/wood/" + ctx.getName()))
				.texture("particle", pvd.mcLoc("block/birch_planks"))
				.renderType("cutout");

		var builder = pvd.getMultipartBuilder(ctx.get());
		builder.part().modelFile(stall).addModel().end();
		TableClothImpl.buildStates(builder, pvd, table);

	}

	public static void genRecipe(RegistrateRecipeProvider pvd, GLDecoBlocks.WoodType e, Supplier<? extends ItemLike> self) {
		GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, self.get(), 4)::unlockedBy, Items.STICK)
				.pattern("WWW").pattern(" S ").pattern(" P ")
				.define('W', e.strippedWood)
				.define('S', Items.STICK)
				.define('P', e.slab().value())
				.save(pvd);
	}

	public static void genLoot(RegistrateBlockLootTables pvd, DelegateBlock block) {
		pvd.add(block, TableClothImpl.loot(pvd, block));
	}

}
