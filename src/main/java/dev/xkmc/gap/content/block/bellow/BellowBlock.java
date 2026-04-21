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

public class BellowBlock implements ScheduleTickBlockMethod {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GapRegistries.BELLOW_BE, BellowBlockEntity.class);

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		if (level.getBlockEntity(pos) instanceof BellowBlockEntity be) {
			be.click();
		}
	}

	public static void buildModel(DataGenContext<Block, DelegateBlock> ctx, RegistrateBlockstateProvider pvd) {
	}

}
