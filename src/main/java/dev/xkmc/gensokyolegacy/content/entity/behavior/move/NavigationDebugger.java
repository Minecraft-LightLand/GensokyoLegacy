package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.network.PathDataToClient;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class NavigationDebugger {

	private static final int DIST_SQR = 48 * 48;

	public static boolean hasDebugGlass(ServerPlayer sp) {
		return sp.getItemBySlot(EquipmentSlot.HEAD).is(GLItems.DEBUG_GLASSES);
	}

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
		ArrayList<Vec3> list = new ArrayList<>();
		list.add(self.position());
		for (var i = path.getNextNodeIndex(); i < path.getNodeCount(); i++) {
			list.add(Vec3.atCenterOf(path.getNodePos(i)));
		}
		var packet = new PathDataToClient(self.getId(), list);
		for (var e : self.getPlayers()) {
			if (self.distanceToSqr(e) < DIST_SQR && hasDebugGlass(e))
				GensokyoLegacy.HANDLER.toClientPlayer(packet, e);
		}
	}

}
