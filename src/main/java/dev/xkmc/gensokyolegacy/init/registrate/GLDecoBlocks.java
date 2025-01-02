package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.block.variants.VerticalSlabBlock;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class GLDecoBlocks {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final BrickSet PACKED_ICE_SET, SNOW_SET, ICE_BRICK_SET, SNOW_BRICK_SET;

	public static final StoneAndBrickSet DARKSTONE;

	static {
		var reg = GensokyoLegacy.REGISTRATE;
		TAB = reg.buildModCreativeTab("building_blocks", "Gensokyo Legacy - Building Blocks",
				e -> e.icon(() -> GLDecoBlocks.ICE_BRICK_SET.block.get().asItem().getDefaultInstance()));

		SNOW_SET = new BrickSet(reg, "snow", BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK),
				ResourceLocation.withDefaultNamespace("block/snow"), () -> Blocks.SNOW_BLOCK,
				BlockTags.MINEABLE_WITH_SHOVEL);
		SNOW_BRICK_SET = new BrickSet(reg, "snow", BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)
				.requiresCorrectToolForDrops().strength(0.2F).sound(SoundType.SNOW),
				(ctx, pvd) -> GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get())::unlockedBy, Items.SNOW_BLOCK)
						.pattern("XX").pattern("XX").define('X', Items.SNOW_BLOCK).save(pvd));

		PACKED_ICE_SET = new BrickSet(reg, "packed_ice", BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE),
				ResourceLocation.withDefaultNamespace("block/packed_ice"), () -> Blocks.PACKED_ICE,
				BlockTags.MINEABLE_WITH_PICKAXE);

		ICE_BRICK_SET = new BrickSet(reg, "ice", BlockBehaviour.Properties.of().mapColor(MapColor.ICE)
				.instrument(NoteBlockInstrument.CHIME)
				.requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.GLASS),
				(ctx, pvd) -> pvd.stonecutting(DataIngredient.items(Blocks.PACKED_ICE), RecipeCategory.BUILDING_BLOCKS, ctx));

		DARKSTONE = new StoneAndBrickSet(reg, "darkstone", MapColor.COLOR_BLACK, 1F,
				SoundType.DEEPSLATE, SoundType.DEEPSLATE_BRICKS);

	}

	public static void register() {

	}

	public static class BrickSet {

		public final Supplier<Block> block;
		public final BlockEntry<StairBlock> stairs;
		public final BlockEntry<SlabBlock> slab;
		public final BlockEntry<VerticalSlabBlock> vertical;

		private boolean suppressCraft;

		public BrickSet(L2Registrate reg, String id, BlockBehaviour.Properties prop,
						NonNullBiConsumer<DataGenContext<Block, Block>, RegistrateRecipeProvider> recipe) {
			this(reg, id + "_brick", prop, GensokyoLegacy.loc("block/" + id + "_bricks"),
					reg.block(id + "_bricks", p -> new Block(prop))
							.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
							.tag(BlockTags.MINEABLE_WITH_PICKAXE).recipe(recipe)
							.simpleItem().register(),
					BlockTags.MINEABLE_WITH_PICKAXE);
		}

		public BrickSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, TagKey<Block> tool) {
			this(reg, id, prop, GensokyoLegacy.loc("block/" + id),
					reg.block(id, p -> new Block(prop))
							.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
							.tag(tool).simpleItem().register(), tool);
		}

		public BrickSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, ResourceLocation side, Supplier<Block> base, TagKey<Block> tool) {
			block = base;
			stairs = reg.block(id + "_stairs", p ->
							new StairBlock(block.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(tool, BlockTags.STAIRS).item().tag(ItemTags.STAIRS).build()
					.recipe(this::genStair).register();
			slab = reg.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
					.tag(tool, BlockTags.SLABS).item().tag(ItemTags.SLABS).build()
					.recipe(this::genSlab).register();
			vertical = reg.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, side, side))
					.tag(YHTagGen.VERTICAL_SLAB, tool).item().build()
					.recipe(this::genVertical).register();
		}

		private BrickSet suppressCraft() {
			suppressCraft = true;
			return this;
		}

		private void genStair(DataGenContext<Block, StairBlock> ctx, RegistrateRecipeProvider pvd) {
			if (suppressCraft) {
				pvd.stonecutting(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx);
				return;
			}
			pvd.stairs(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
		}

		private void genSlab(DataGenContext<Block, SlabBlock> ctx, RegistrateRecipeProvider pvd) {
			if (suppressCraft) {
				pvd.stonecutting(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, 2);
				return;
			}
			pvd.slab(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true);
		}

		private void genVertical(DataGenContext<Block, VerticalSlabBlock> ctx, RegistrateRecipeProvider pvd) {
			if (suppressCraft) {
				pvd.stonecutting(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, 2);
				return;
			}
			VerticalSlabBlock.genRecipe(pvd, block, ctx);
		}

	}

	public static class StoneAndBrickSet {

		public BrickSet stone, brick;
		public BlockEntry<Block> chiseled;

		public StoneAndBrickSet(L2Registrate reg, String id, MapColor color, float strength, SoundType stoneSound, SoundType brickSound) {
			stone = new BrickSet(reg, id, BlockBehaviour.Properties.of().mapColor(color)
					.requiresCorrectToolForDrops().strength(strength).sound(stoneSound),
					BlockTags.MINEABLE_WITH_PICKAXE);
			var brickProp = BlockBehaviour.Properties.of().mapColor(color)
					.requiresCorrectToolForDrops().strength(strength).sound(brickSound);
			brick = new BrickSet(reg, id, brickProp, this::brick);
			chiseled = reg.block("chiseled_" + id + "_bricks", Block::new)
					.properties(p -> brickProp).defaultBlockstate()
					.tag(BlockTags.MINEABLE_WITH_PICKAXE)
					.simpleItem()
					.recipe(this::chisel)
					.register();
		}

		private void chisel(DataGenContext<Block, Block> ctx, RegistrateRecipeProvider pvd) {
			pvd.stonecutting(DataIngredient.items(stone.block.get()), RecipeCategory.BUILDING_BLOCKS, ctx);
			pvd.stonecutting(DataIngredient.items(brick.block.get()), RecipeCategory.BUILDING_BLOCKS, ctx);
			GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get())::unlockedBy,
							brick.slab.asItem()).pattern("A").pattern("A").define('A', brick.slab.asItem())
					.save(pvd);
		}

		private void brick(DataGenContext<Block, Block> ctx, RegistrateRecipeProvider pvd) {
			pvd.stonecutting(DataIngredient.items(stone.block.get()), RecipeCategory.BUILDING_BLOCKS, ctx);
			GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 4)::unlockedBy,
							brick.block.get().asItem()).pattern("AA").pattern("AA").define('A', brick.block.get())
					.save(pvd);
		}


	}

}
