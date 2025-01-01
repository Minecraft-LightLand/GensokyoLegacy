package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.block.variants.VerticalSlabBlock;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class GLDecoBlocks {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final BrickSet PACKED_ICE_SET, SNOW_SET, ICE_BRICK_SET, SNOW_BRICK_SET,
			DARKSTONE, DARKSTONE_BRICK;

	static {
		TAB = GensokyoLegacy.REGISTRATE.buildModCreativeTab("building_blocks", "Gensokyo Legacy - Building Blocks",
				e -> e.icon(() -> GLDecoBlocks.ICE_BRICK_SET.block.get().asItem().getDefaultInstance()));

		PACKED_ICE_SET = new BrickSet("packed_ice", BlockBehaviour.Properties.ofFullCopy(Blocks.PACKED_ICE),
				ResourceLocation.withDefaultNamespace("block/packed_ice"), () -> Blocks.PACKED_ICE,
				BlockTags.MINEABLE_WITH_PICKAXE);
		SNOW_SET = new BrickSet("snow", BlockBehaviour.Properties.ofFullCopy(Blocks.SNOW_BLOCK),
				ResourceLocation.withDefaultNamespace("block/snow"), () -> Blocks.SNOW_BLOCK,
				BlockTags.MINEABLE_WITH_SHOVEL);

		ICE_BRICK_SET = new BrickSet("ice", BlockBehaviour.Properties.of().mapColor(MapColor.ICE)
				.instrument(NoteBlockInstrument.CHIME)
				.requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.GLASS));
		SNOW_BRICK_SET = new BrickSet("snow", BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)
				.requiresCorrectToolForDrops().strength(0.2F).sound(SoundType.SNOW));

		DARKSTONE = new BrickSet("darkstone", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
				.requiresCorrectToolForDrops().strength(1F).sound(SoundType.DEEPSLATE),
				BlockTags.MINEABLE_WITH_PICKAXE);
		DARKSTONE_BRICK = new BrickSet("darkstone", BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)
				.requiresCorrectToolForDrops().strength(1F).sound(SoundType.DEEPSLATE_BRICKS));

	}

	public static void register() {

	}

	public static class BrickSet {

		public final Supplier<Block> block;
		public final BlockEntry<StairBlock> stairs;
		public final BlockEntry<SlabBlock> slab;
		public final BlockEntry<VerticalSlabBlock> vertical;

		private boolean suppressCraft;

		public BrickSet(String id, BlockBehaviour.Properties prop) {
			this(id + "_brick", prop, GensokyoLegacy.loc("block/" + id + "_bricks"),
					GensokyoLegacy.REGISTRATE.block(id + "_bricks", p -> new Block(prop))
							.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
							.tag(BlockTags.MINEABLE_WITH_PICKAXE)
							.simpleItem().register(),
					BlockTags.MINEABLE_WITH_PICKAXE);
			suppressCraft = true;
		}

		public BrickSet(String id, BlockBehaviour.Properties prop, TagKey<Block> tool) {
			this(id, prop, GensokyoLegacy.loc("block/" + id),
					GensokyoLegacy.REGISTRATE.block(id, p -> new Block(prop))
							.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
							.tag(tool).simpleItem().register(), tool);
		}

		public BrickSet(String id, BlockBehaviour.Properties prop, ResourceLocation side, Supplier<Block> base, TagKey<Block> tool) {
			block = base;
			stairs = GensokyoLegacy.REGISTRATE.block(id + "_stairs", p ->
							new StairBlock(block.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(tool, BlockTags.STAIRS).item().tag(ItemTags.STAIRS).build()
					.recipe(this::genStair).register();
			slab = GensokyoLegacy.REGISTRATE.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
					.tag(tool, BlockTags.SLABS).item().tag(ItemTags.SLABS).build()
					.recipe(this::genSlab).register();
			vertical = GensokyoLegacy.REGISTRATE.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> VerticalSlabBlock.buildBlockState(ctx, pvd, side, side))
					.tag(YHTagGen.VERTICAL_SLAB, tool).item().build()
					.recipe(this::genVertical).register();
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

}
