package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class NavigationDebugger {

	private final YoukaiEntity self;
	private Path cache;

	public NavigationDebugger(YoukaiEntity self) {
		this.self = self;
	}

	public void debugPath() {
		var path = self.getNavigation().getPath();
		if (path == null) {
			cache = null;
			return;
		}
		if (path == cache) {
			if (self.tickCount % 10 != 0)
				return;
		}
		cache = path;
		Vec3 current = self.position();
		for (var i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
			var pos = Vec3.atCenterOf(path.getNodePos(i));
			var len = pos.distanceTo(current);
			int n = (int) ((len + 1) * 4);
			for (int j = 0; j < n; j++) {
				double p = (j + 1d) / n;
				Vec3 c = current.lerp(pos, p);
				((ServerLevel) self.level()).sendParticles(ParticleTypes.SMALL_FLAME,
						c.x, c.y, c.z, 0, 0, 0, 0, 0);
			}
			current = pos;
		}
	}

}
