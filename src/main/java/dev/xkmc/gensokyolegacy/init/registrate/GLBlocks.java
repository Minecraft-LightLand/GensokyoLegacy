package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.content.block.bed.FlatBedShape;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlock;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationBoxBlock;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationBoxBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationShape;
import dev.xkmc.gensokyolegacy.content.block.donation.DoubleBlockHorizontal;
import dev.xkmc.gensokyolegacy.content.block.misc.BookPile;
import dev.xkmc.gensokyolegacy.content.block.misc.BookStack;
import dev.xkmc.gensokyolegacy.content.block.portal.*;
import dev.xkmc.gensokyolegacy.content.block.shelf.ShelfBlock;
import dev.xkmc.gensokyolegacy.content.block.shelf.ShelfBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.shelf.ShelfRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.Locale;

public class GLBlocks {

	public enum Beds {
		CIRNO(Blocks.BLUE_BED),
		RUMIA(Blocks.BLACK_BED),
		REIMU(Blocks.RED_BED),
		MORICHIKA(Blocks.LIGHT_BLUE_BED);

		private final BedBlock template;
		private final DyeColor wool;

		Beds(Block template) {
			this.template = (BedBlock) template;
			this.wool = this.template.getColor();
		}

		public YoukaiBedBlock get() {
			return BEDS[ordinal()].get();
		}

		public Holder<Block> holder() {
			return BEDS[ordinal()];
		}
	}

	public static final BlockEntry<DonationBoxBlock> DONATION_BOX;
	public static final BlockEntityEntry<DonationBoxBlockEntity> DONATION_BOX_BE;

	public static final BlockEntry<DelegateBlock> SHELF;
	public static final BlockEntityEntry<ShelfBlockEntity> SHELF_BE;

	public static final BlockEntry<BasePortalBlock> GAP_PORTAL;
	public static final BlockEntityEntry<GapPortalBlockEntity> GAP_BE;

	public static final BlockEntry<DelegateBlock> BOOK_PILE, BOOK_STACK, ALCHEMY_POT;

	public static final BlockEntry<YoukaiBedBlock>[] BEDS;
	public static final BlockEntityEntry<YoukaiBedBlockEntity> BE_BED;

	static {

		{

			DONATION_BOX = GensokyoLegacy.REGISTRATE.block("donation_box", p -> new DonationBoxBlock(
							BlockBehaviour.Properties.of().noLootTable().strength(2.0F).sound(SoundType.WOOD)
									.mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS),
							BlockTemplates.HORIZONTAL, new DoubleBlockHorizontal(), new DonationShape(), DonationBoxBlock.TE
					)).blockstate(DonationBoxBlock::buildStates)
					.simpleItem()
					.loot((pvd, block) -> pvd.add(block, LootTable.lootTable()))
					.register();

			DONATION_BOX_BE = GensokyoLegacy.REGISTRATE.blockEntity("donation_box", DonationBoxBlockEntity::new)
					.validBlock(DONATION_BOX)
					.register();

			SHELF = GensokyoLegacy.REGISTRATE.block("birch_shelf", p -> DelegateBlock.newBaseBlock(p,
							BlockTemplates.HORIZONTAL, new ShelfBlock(), ShelfBlock.BE))
					.initialProperties(() -> Blocks.BIRCH_TRAPDOOR)
					.blockstate(ShelfBlock::buildStates)
					.tag(BlockTags.MINEABLE_WITH_AXE)
					.simpleItem().register();

			SHELF_BE = GensokyoLegacy.REGISTRATE.blockEntity("shelf", ShelfBlockEntity::new)
					.validBlock(SHELF)
					.renderer(() -> ShelfRenderer::new)
					.register();

		}

		{
			GAP_PORTAL = GensokyoLegacy.REGISTRATE.block("gap_portal", GapPortalBlock::of)
					.initialProperties(() -> Blocks.END_PORTAL)
					.properties(p -> p.noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().getBuilder(ctx.getName()).parent(new ModelFile.UncheckedModelFile("builtin/entity"))
									.texture("particle", pvd.mcLoc("block/obsidian"))))
					.item(GapPortalItem::new).model((ctx, pvd) ->
							pvd.generated(ctx, pvd.modLoc("item/gap"))).build()
					.register();

			GAP_BE = GensokyoLegacy.REGISTRATE.blockEntity("gap_portal", GapPortalBlockEntity::new)
					.validBlock(GAP_PORTAL)
					.renderer(() -> GapPortalRenderer::new)
					.register();
		}

		{
			BOOK_PILE = GensokyoLegacy.REGISTRATE.block("book_pile", BookPile::create)
					.properties(p -> p.noOcclusion().strength(0F).offsetType(BlockBehaviour.OffsetType.XZ)
							.mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).dynamicShape())
					.blockstate(BookPile::buildStates).loot(BookPile::buildLoot).simpleItem()
					.recipe((ctx, pvd) -> GLRecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(
							RecipeCategory.DECORATIONS, ctx.get(), 1)::unlockedBy, Items.BOOK).requires(Items.BOOK, 5).save(pvd))
					.register();

			BOOK_STACK = GensokyoLegacy.REGISTRATE.block("book_stack", BookStack::create)
					.properties(p -> p.noOcclusion().strength(0F).offsetType(BlockBehaviour.OffsetType.XZ)
							.mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY).dynamicShape())
					.blockstate(BookStack::buildStates).loot(BookStack::buildLoot).simpleItem()
					.recipe((ctx, pvd) -> GLRecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(
							RecipeCategory.DECORATIONS, ctx.get(), 1)::unlockedBy, Items.BOOK).requires(Items.BOOK, 5).save(pvd))
					.register();

			ALCHEMY_POT = GensokyoLegacy.REGISTRATE.block("alchemy_pot", p -> DelegateBlock.newBaseBlock(p))
					.initialProperties(() -> Blocks.ANVIL)
					.properties(p -> p.noOcclusion())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().getBuilder(ctx.getName())
									.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/" + ctx.getName())))
									.texture("all", pvd.modLoc("block/utensil/" + ctx.getName()))
									.renderType("cutout")
					))
					.simpleItem()
					.register();
		}

		BEDS = new BlockEntry[Beds.values().length];
		for (var e : Beds.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			BEDS[e.ordinal()] = GensokyoLegacy.REGISTRATE.block(name + "_bed", YoukaiBedBlock::new)
					.initialProperties(() -> e.template)
					.blockstate(FlatBedShape::buildStates)
					.item(BedItem::new)
					.model(FlatBedShape::buildItemModel)
					.build()
					.loot(YoukaiBedBlock::buildLoot)
					.register();
		}
		BE_BED = GensokyoLegacy.REGISTRATE.blockEntity("youkai_bed", YoukaiBedBlockEntity::new)
				.validBlocks(BEDS)
				.register();
	}

	public static void register() {

	}

}
