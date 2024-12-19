package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.gensokyolegacy.content.block.base.ICustomFixBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public record BlockFix(BlockPos pos, BlockState state) {

	public void fix(ServerLevel level) {
		if (state.getBlock() instanceof ICustomFixBlock block)
			block.onFix(level, pos, state);
		else if (state.getBlock() instanceof DoorBlock) {
			level.setBlockAndUpdate(pos, state);
			if (state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
				level.setBlock(pos.above(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER), 2);
			} else {
				level.setBlockAndUpdate(pos.below(), state.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
			}
		} else level.setBlockAndUpdate(pos, state);
	}

}
