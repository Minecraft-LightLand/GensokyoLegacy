package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public record PathDataToClient(
		int id, ArrayList<Vec3> pos
) implements SerialPacketBase<PathDataToClient> {

	@Override
	public void handle(Player player) {
		var e = player.level().getEntity(id);
		if (!(e instanceof YoukaiEntity self)) return;
		Vec3 current = self.position();
		for (var p : pos) {
			var len = p.distanceTo(current);
			int n = (int) ((len + 1) * 4);
			for (int j = 0; j < n; j++) {
				double perc = (j + 1d) / n;
				Vec3 c = current.lerp(p, perc);
				self.level().addParticle(ParticleTypes.SMALL_FLAME,
						c.x, c.y, c.z, 0, 0, 0);
			}
			current = p;
		}
	}

}
