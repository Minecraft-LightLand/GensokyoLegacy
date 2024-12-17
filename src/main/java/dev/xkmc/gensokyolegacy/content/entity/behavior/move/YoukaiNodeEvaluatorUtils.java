package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;

public class YoukaiNodeEvaluatorUtils {

	public static PathType getPathType(PathType ans, PathfindingContext context, int x, int y, int z) {
		if (ans == PathType.TRAPDOOR || ans == PathType.DANGER_TRAPDOOR) {
			var state = context.getBlockState(new BlockPos(x, y, z));
			if (state.getBlock() instanceof TrapDoorBlock && state.getValue(TrapDoorBlock.OPEN))
				return PathType.BLOCKED;
		}
		return ans;
	}

}
