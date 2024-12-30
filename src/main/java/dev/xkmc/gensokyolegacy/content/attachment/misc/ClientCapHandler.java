package dev.xkmc.gensokyolegacy.content.attachment.misc;

import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.gensokyolegacy.init.registrate.GLSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientCapHandler {

	public static void frogUpdate(FrogSyncPacket packet) {
		Level level = Minecraft.getInstance().level;
		if (level != null) {
			Entity entity = level.getEntity(packet.id());
			if (entity instanceof Frog frog) {
				if (GLMeta.FROG_GOD.type().isProper(frog)) {
					GLMeta.FROG_GOD.type().getOrCreate(frog).hasHat = packet.hasHat();
				}
			}
		}
	}

	public static void koishiAttack(KoishiStartPacket.Type type, Vec3 pos) {
		Player player = Minecraft.getInstance().player;
		if (player == null) return;
		if (type == KoishiStartPacket.Type.START)
			player.playSound(GLSounds.KOISHI_RING.get(), 1, 1);
		else if (type == KoishiStartPacket.Type.PARTICLE) {
			GLMeta.KOISHI_ATTACK.type().getOrCreate(player).startParticle(pos);
		}
	}

	public static void showParticle(Player player, Vec3 source) {
		var r = player.level().random;
		var p = source.add(new Vec3(r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1)
				.normalize().scale(0.3));
		player.level().addAlwaysVisibleParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, p.x, p.y, p.z, 0, 0, 0);
	}

}
