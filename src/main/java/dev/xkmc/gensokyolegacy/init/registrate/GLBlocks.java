package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.content.block.bed.GLBEWLR;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlock;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedRenderer;
import dev.xkmc.gensokyolegacy.content.block.censer.CenserBlock;
import dev.xkmc.gensokyolegacy.content.block.censer.CenserBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.censer.CenserRenderer;
import dev.xkmc.gensokyolegacy.content.block.deco.BooksBlock;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationBoxBlock;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationBoxBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.donation.DonationShape;
import dev.xkmc.gensokyolegacy.content.block.donation.DoubleBlockHorizontal;
import dev.xkmc.gensokyolegacy.content.block.mistletoe.MistletoeFoliageBlock;
import dev.xkmc.gensokyolegacy.content.block.mistletoe.MistletoeLeavesBlock;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLRecipeGen;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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

import java.util.Locale;

public class GLBlocks {

	public enum Beds {
		CIRNO(Blocks.BLUE_BED),
		RUMIA(Blocks.BLUE_BED),
		REIMU(Blocks.RED_BED);

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

	public static final BlockEntry<MistletoeLeavesBlock> MISTLETOE_LEAVES;
	public static final BlockEntry<DelegateBlock> MISTLETOE_FOLIAGE;

	public static final BlockEntry<DonationBoxBlock> DONATION_BOX;
	public static final BlockEntityEntry<DonationBoxBlockEntity> DONATION_BOX_BE;

	public static final BlockEntry<DelegateBlock> CENSER;
	public static final BlockEntityEntry<CenserBlockEntity> CENSER_BE;

	public static final BlockEntry<DelegateBlock> BOOKS;

	public static final BlockEntry<YoukaiBedBlock>[] BEDS;
	public static final BlockEntityEntry<YoukaiBedBlockEntity> BE_BED;

	static {

		MISTLETOE_LEAVES = GensokyoLegacy.REGISTRATE.block("mistletoe_leaves", p ->
						new MistletoeLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)))
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().withExistingParent(ctx.getName(), "block/leaves")
						.texture("all", pvd.modLoc("block/" + ctx.getName()))))
				.loot(MistletoeLeavesBlock::buildLeavesLoot)
				.tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
				.item().tag(ItemTags.LEAVES).build()
				.register();

		MISTLETOE_FOLIAGE = GensokyoLegacy.REGISTRATE.block("mistletoe_foliage", p -> MistletoeFoliageBlock.create())
				.blockstate(MistletoeFoliageBlock::buildModels)
				.loot(MistletoeLeavesBlock::buildFoliageLoot)
				.tag(BlockTags.MINEABLE_WITH_HOE)
				.item().build()
				.register();

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

		CENSER = GensokyoLegacy.REGISTRATE.block("censer", p -> CenserBlock.create())
				.blockstate(CenserBlock::buildStates)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.simpleItem().register();

		CENSER_BE = GensokyoLegacy.REGISTRATE.blockEntity("censer", CenserBlockEntity::new)
				.validBlock(CENSER).renderer(() -> CenserRenderer::new).register();

		BOOKS = GensokyoLegacy.REGISTRATE.block("book_pile", p -> DelegateBlock.newBaseBlock(
						BlockBehaviour.Properties.of().noOcclusion().strength(0F)
								.mapColor(MapColor.WOOD).sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)
						, BlockTemplates.HORIZONTAL, new BooksBlock()))
				.blockstate(BooksBlock::buildStates).loot(BooksBlock::buildLoot).simpleItem()
				.recipe((ctx, pvd) -> GLRecipeGen.unlock(pvd, ShapelessRecipeBuilder.shapeless(
						RecipeCategory.DECORATIONS, ctx.get(), 1)::unlockedBy, Items.BOOK).requires(Items.BOOK, 5).save(pvd))
				.register();

		BEDS = new BlockEntry[Beds.values().length];
		for (var e : Beds.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			BEDS[e.ordinal()] = GensokyoLegacy.REGISTRATE.block(name + "_bed", YoukaiBedBlock::new)
					.initialProperties(() -> e.template)
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getExistingFile(pvd.mcLoc("block/bed"))))
					.item(BedItem::new)
					.model((ctx, pvd) -> pvd.withExistingParent(ctx.getName(), pvd.mcLoc("item/template_bed"))
							.texture("particle", pvd.mcLoc("block/" + e.wool.getName() + "_wool")))
					.clientExtension(() -> () -> GLBEWLR.EXTENSIONS)
					.build()
					.loot(YoukaiBedBlock::buildLoot)
					.register();
		}
		BE_BED = GensokyoLegacy.REGISTRATE.blockEntity("youkai_bed", YoukaiBedBlockEntity::new)
				.renderer(() -> YoukaiBedRenderer::new)
				.validBlocks(BEDS)
				.register();
	}

	public static void register() {

	}

}
