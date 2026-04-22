package dev.xkmc.gap.content.block.bellow;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.gap.content.block.pot.PotBlockEntity;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.ScheduleTickBlockMethod;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.ModelFile;

public class BellowBlock implements ScheduleTickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GapRegistries.BELLOW_BE, BellowBlockEntity.class);

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		if (level.getBlockEntity(pos) instanceof BellowBlockEntity be) {
			be.click();
		}
	}

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
