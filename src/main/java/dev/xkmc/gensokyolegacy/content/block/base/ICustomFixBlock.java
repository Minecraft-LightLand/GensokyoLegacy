package dev.xkmc.gensokyolegacy.content.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface ICustomFixBlock {

	void onFix(ServerLevel level, BlockPos pos, BlockState state);

}
