package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;

public class YoukaiFlyNodeEvaluator extends FlyNodeEvaluator {

	@Override
	public PathType getPathType(PathfindingContext context, int x, int y, int z) {
		var ans = super.getPathType(context, x, y, z);
		return YoukaiNodeEvaluatorUtils.getPathType(ans, context, x, y, z);
	}

}
