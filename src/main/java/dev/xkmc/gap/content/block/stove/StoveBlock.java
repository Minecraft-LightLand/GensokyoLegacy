package dev.xkmc.gap.content.block.stove;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class StoveBlock {


	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/stove_block")
				.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/utensil/stove_block")))
				.texture("side", pvd.modLoc("block/utensil/stove_side"))
				.texture("top", pvd.modLoc("block/utensil/stove_top"))
				.texture("bottom", pvd.modLoc("block/utensil/stove_bottom"))
				.texture("front", pvd.modLoc("block/utensil/stove_front"))
				.renderType("cutout"));
	}
}
