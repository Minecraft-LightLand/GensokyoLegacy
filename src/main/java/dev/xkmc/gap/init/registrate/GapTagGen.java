package dev.xkmc.gap.init.registrate;

import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.gensokyolegacy.init.data.GLTagGen;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class GapTagGen {

	public static final TagKey<Block> HEAT_SOURCE = GLTagGen.block("heat_source");

	public static void genBlocks(RegistrateTagsProvider.IntrinsicImpl<Block> pvd) {
		pvd.addTag(HEAT_SOURCE).add(Blocks.FIRE, Blocks.LAVA, Blocks.CAMPFIRE, Blocks.SOUL_FIRE, Blocks.SOUL_CAMPFIRE);
	}
}
