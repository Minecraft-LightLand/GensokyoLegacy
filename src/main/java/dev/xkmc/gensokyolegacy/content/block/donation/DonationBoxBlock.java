package dev.xkmc.gensokyolegacy.content.block.donation;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import dev.xkmc.gensokyolegacy.content.block.base.ICustomFixBlock;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.l2modularblock.core.BlockTemplates;
import dev.xkmc.l2modularblock.core.DelegateEntityBlockImpl;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.type.BlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DonationBoxBlock extends DelegateEntityBlockImpl implements ICustomFixBlock {

	public static final BlockMethod TE = new BlockEntityBlockMethodImpl<>(GLBlocks.DONATION_BOX_BE, DonationBoxBlockEntity.class);

	public DonationBoxBlock(Properties p, BlockMethod... impl) {
		super(p, impl);
	}

	@Override
	public void onFix(ServerLevel level, BlockPos pos, BlockState state) {
		level.setBlock(pos, state, 2);
		var dir = state.getValue(BlockTemplates.HORIZONTAL_FACING).getOpposite();
		level.setBlockAndUpdate(pos.relative(dir), state.setValue(BlockTemplates.HORIZONTAL_FACING, dir));
	}

	public static void buildStates(DataGenContext<Block, DonationBoxBlock> ctx, RegistrateBlockstateProvider pvd) {
		pvd.horizontalBlock(ctx.get(), pvd.models().cube("block/" + ctx.getName(),
				pvd.modLoc("block/" + ctx.getName() + "_down"),
				pvd.modLoc("block/" + ctx.getName() + "_up"),
				pvd.modLoc("block/" + ctx.getName() + "_end"),
				pvd.modLoc("block/" + ctx.getName() + "_empty"),
				pvd.modLoc("block/" + ctx.getName() + "_side"),
				pvd.modLoc("block/" + ctx.getName() + "_side")
		).texture("particle", pvd.modLoc("block/" + ctx.getName() + "_down")));
	}


}
