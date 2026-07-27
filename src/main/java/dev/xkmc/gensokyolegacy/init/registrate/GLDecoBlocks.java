package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.xkmc.gensokyolegacy.content.block.deco.IBlockSet;
import dev.xkmc.gensokyolegacy.content.block.deco.TatamiBlock;
import dev.xkmc.gensokyolegacy.content.block.deco.VerticalSlabBlock;
import dev.xkmc.gensokyolegacy.content.block.furniture.CushionBlock;
import dev.xkmc.gensokyolegacy.content.block.furniture.WoodChairBlock;
import dev.xkmc.gensokyolegacy.content.block.furniture.WoodTableBlock;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Locale;
import java.util.function.Supplier;

public class GLDecoBlocks {


	public enum WoodType implements IBlockSet {
		OAK(Blocks.OAK_PLANKS, Blocks.OAK_FENCE, Items.STRIPPED_OAK_WOOD, Blocks.OAK_SLAB, Blocks.OAK_STAIRS),
		BIRCH(Blocks.BIRCH_PLANKS, Blocks.BIRCH_FENCE, Items.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_SLAB, Blocks.BRICK_STAIRS),
		SPRUCE(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_FENCE, Items.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_STAIRS),
		JUNGLE(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_FENCE, Items.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_STAIRS),
		DARK_OAK(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_FENCE, Items.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_STAIRS),
		ACACIA(Blocks.ACACIA_PLANKS, Blocks.ACACIA_FENCE, Items.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_SLAB, Blocks.ACACIA_STAIRS),
		CRIMSON(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_FENCE, Items.STRIPPED_CRIMSON_HYPHAE, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_STAIRS),
		WARPED(Blocks.WARPED_PLANKS, Blocks.WARPED_FENCE, Items.STRIPPED_WARPED_HYPHAE, Blocks.WARPED_SLAB, Blocks.WARPED_STAIRS),
		MANGROVE(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_FENCE, Items.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_STAIRS),
		CHERRY(Blocks.CHERRY_PLANKS, Blocks.CHERRY_FENCE, Items.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_SLAB, Blocks.CHERRY_STAIRS),
		BAMBOO(Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_FENCE, Items.STRIPPED_BAMBOO_BLOCK, Blocks.BAMBOO_SLAB, Blocks.BAMBOO_STAIRS),
		;

		private final Block plankProp, fenceProp, slab, stairs;
		private final String name;
		private final ResourceLocation tex;
		public final ItemLike plank, strippedWood;
		public BlockEntry<WoodTableBlock> table;
		public BlockEntry<WoodChairBlock> seat;
		public BlockEntry<VerticalSlabBlock> vertical;

		WoodType(Block plankProp, Block fenceProp, ItemLike strippedWood, Block slab, Block stairs) {
			this.plankProp = plankProp;
			this.fenceProp = fenceProp;
			this.plank = plankProp;
			this.strippedWood = strippedWood;
			this.slab = slab;
			this.stairs = stairs;
			name = name().toLowerCase(Locale.ROOT);
			tex = ResourceLocation.withDefaultNamespace("block/" + name + "_planks");
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public BlockBehaviour.Properties prop() {
			return BlockBehaviour.Properties.ofFullCopy(plankProp);
		}

		@Override
		public Holder<Block> base() {
			return plankProp.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> stairs() {
			return stairs.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> slab() {
			return slab.builtInRegistryHolder();
		}

		@Override
		public Holder<Block> vertical() {
			return vertical;
		}

		@Override
		public ResourceLocation top() {
			return tex;
		}

		@Override
		public ResourceLocation side() {
			return tex;
		}

	}

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

		new StoneAndBrickSet(reg, "darkstone", MapColor.COLOR_BLACK, 1F,
				SoundType.DEEPSLATE, SoundType.DEEPSLATE_BRICKS);

		var tiles = new DyeColor[]{DyeColor.CYAN, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.BROWN, DyeColor.BLUE, DyeColor.BLACK};
		var strips = new DyeColor[]{DyeColor.BLUE};

		for (DyeColor col : tiles) {
			new BrickSet(reg, col.getName() + "_tiles",
					BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).mapColor(MapColor.byId(14 + col.getId())),
					"tiles/", BlockTags.MINEABLE_WITH_PICKAXE);
		}

		for (DyeColor col : strips) {
			reg.block(col.getName() + "_strips_terracota", Block::new)
					.initialProperties(() -> Blocks.WHITE_GLAZED_TERRACOTTA)
					.properties(p -> p.mapColor(MapColor.byId(14 + col.getId())))
					.blockstate((ctx, pvd) ->
							pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), GensokyoLegacy.loc("block/strips/" + ctx.getName()))))
					.tag(BlockTags.MINEABLE_WITH_PICKAXE).simpleItem().register();
		}

		for (var e : WoodType.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);

			e.table = reg.block(name + "_dining_table", p -> new WoodTableBlock(
							BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
					.blockstate(WoodTableBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.recipe((ctx, pvd) -> WoodTableBlock.genRecipe(pvd, e, ctx))
					.register();

			e.seat = reg.block(name + "_dining_chair", p -> new WoodChairBlock(
							BlockBehaviour.Properties.ofFullCopy(e.plankProp)))
					.blockstate(WoodChairBlock::buildStates)
					.simpleItem().tag(BlockTags.MINEABLE_WITH_AXE)
					.recipe((ctx, pvd) -> WoodChairBlock.genRecipe(pvd, e, ctx))
					.register();
		}

		reg.block("tatami", p -> DelegateBlock.newBaseBlock(p, BlockTemplates.HORIZONTAL, new TatamiBlock()))
				.properties(p -> p.mapColor(MapColor.SAND).strength(0).sound(SoundType.WOOL))
				.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), state -> {
					var kind = state.getValue(TatamiBlock.KIND);
					var suffix = kind == TatamiBlock.Kind.SQUARE ? "" : "_" + kind.getSerializedName();
					return pvd.models().carpet(ctx.getName() + suffix, pvd.modLoc("block/tatami/tatami" + suffix));
				}))
				.simpleItem()
				.register();

		reg.block("cushion", CushionBlock::new)
				.properties(p -> p.mapColor(MapColor.SAND).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).noOcclusion().noCollission())
				.blockstate(CushionBlock::buildStates)
				.item().tag(GLTagGen.CUSHIONS).build()
				.register();
		for (DyeColor col : DyeColor.values()) {
			reg.block(col.getName() + "_cushion", CushionBlock::new)
					.properties(p -> p.mapColor(MapColor.byId(14 + col.getId())).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY).noOcclusion().noCollission())
					.blockstate(CushionBlock::buildStates)
					.item().tag(GLTagGen.CUSHIONS).build()
					.register();
		}

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

		public BrickSet(L2Registrate reg, String id, BlockBehaviour.Properties prop, String prefix, TagKey<Block> tool) {
			this(reg, id, prop, GensokyoLegacy.loc("block/" + prefix + id),
					reg.block(id, p -> new Block(prop))
							.blockstate((ctx, pvd) ->
									pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), GensokyoLegacy.loc("block/" + prefix + id))))
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
					.tag(GLTagGen.VERTICAL_SLAB, tool).item().build()
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
