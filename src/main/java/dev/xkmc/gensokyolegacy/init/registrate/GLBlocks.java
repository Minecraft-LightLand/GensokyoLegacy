package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.content.block.bed.GLBEWLR;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlock;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

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

	public static final BlockEntry<YoukaiBedBlock>[] BEDS;
	public static final BlockEntityEntry<YoukaiBedBlockEntity> BE_BED;

	static {
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
