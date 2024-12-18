package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.content.block.variants.VerticalSlabBlock;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.function.Supplier;

public class GLDecoBlocks {

	public static final SimpleEntry<CreativeModeTab> TAB;

	public static final BrickSet ICE_SET, SNOW_SET;

	static {
		TAB = GensokyoLegacy.REGISTRATE.buildModCreativeTab("building_blocks", "Gensokyo Legacy - Building Blocks",
				e -> e.icon(() -> GLDecoBlocks.ICE_SET.block.get().asItem().getDefaultInstance()));
		ICE_SET = new BrickSet("ice", BlockBehaviour.Properties.of().mapColor(MapColor.ICE).instrument(NoteBlockInstrument.CHIME)
				.requiresCorrectToolForDrops().strength(0.5F).sound(SoundType.GLASS));
		SNOW_SET = new BrickSet("snow", BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)
				.requiresCorrectToolForDrops().strength(0.2F).sound(SoundType.SNOW));
	}

	public static void register() {

	}

	public static class BrickSet {

		public final Supplier<Block> block;
		public final BlockEntry<StairBlock> stairs;
		public final BlockEntry<SlabBlock> slab;
		public final BlockEntry<VerticalSlabBlock> vertical;

		public BrickSet(String id, BlockBehaviour.Properties prop) {
			this(id + "_brick", prop, GensokyoLegacy.loc("block/" + id + "_bricks"),
					GensokyoLegacy.REGISTRATE.block(id + "_bricks", p -> new Block(prop))
							.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get()))
							.tag(BlockTags.MINEABLE_WITH_PICKAXE)
							.simpleItem().register(),
					BlockTags.MINEABLE_WITH_PICKAXE);
		}

		public BrickSet(String id, BlockBehaviour.Properties prop, ResourceLocation side, Supplier<Block> base, TagKey<Block> tool) {
			block = base;
			stairs = GensokyoLegacy.REGISTRATE.block(id + "_stairs", p ->
							new StairBlock(block.get().defaultBlockState(), prop))
					.blockstate((ctx, pvd) -> pvd.stairsBlock(ctx.get(), id, side))
					.tag(tool, BlockTags.STAIRS)
					.item().tag(ItemTags.STAIRS).build()
					.register();
			slab = GensokyoLegacy.REGISTRATE.block(id + "_slab", p ->
							new SlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.slabBlock(ctx.get(),
							pvd.models().slab(ctx.getName(), side, side, side),
							pvd.models().slabTop(ctx.getName() + "_top", side, side, side),
							new ModelFile.UncheckedModelFile(side)))
					.tag(tool, BlockTags.SLABS)
					.item().tag(ItemTags.SLABS).build()
					.register();
			vertical = GensokyoLegacy.REGISTRATE.block(id + "_vertical_slab", p ->
							new VerticalSlabBlock(prop))
					.blockstate((ctx, pvd) -> pvd.horizontalBlock(ctx.get(), VerticalSlabBlock.buildModel(ctx, pvd)
							.texture("top", side).texture("side", side)))
					.tag(tool).item().build().register();
		}

		public void genRecipe(RegistrateRecipeProvider pvd) {
			pvd.stairs(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS,
					stairs, null, true);
			pvd.slab(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS,
					slab, null, true);
			GLRecipeGen.unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, vertical.get(), 6)::unlockedBy, block.get().asItem())
					.pattern("X").pattern("X").pattern("X")
					.define('X', block.get())
					.save(pvd);
			pvd.stonecutting(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, vertical, 2);
		}

	}

}
