package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.xkmc.gensokyolegacy.content.block.YoukaiBedBlock;
import dev.xkmc.gensokyolegacy.content.block.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.content.block.YoukaiBedRenderer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

public class GLBlocks {

	public enum Beds {

	}

	public static final BlockEntry<YoukaiBedBlock>[] BEDS;
	public static final BlockEntityEntry<YoukaiBedBlockEntity> BE_BED;

	static {
		BEDS = new BlockEntry[Beds.values().length];
		for (var e : Beds.values()) {
			String name = e.name().toLowerCase(Locale.ROOT);
			BEDS[e.ordinal()] = GensokyoLegacy.REGISTRATE.block(name + "_bed", YoukaiBedBlock::new)
					.initialProperties(() -> Blocks.WHITE_BED)
					.item(BedItem::new).build()
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
