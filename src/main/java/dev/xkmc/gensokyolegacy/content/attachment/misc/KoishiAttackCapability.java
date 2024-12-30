package dev.xkmc.gensokyolegacy.content.attachment.misc;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.data.GLAdvGen;
import dev.xkmc.gensokyolegacy.init.data.GLDamageTypes;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import dev.xkmc.gensokyolegacy.init.registrate.GLEffects;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHAdvGen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class KoishiAttackCapability extends PlayerCapabilityTemplate<KoishiAttackCapability> {

	private static final int DELAY = 360, MARK_POS = 300;

	@SerialField
	private int tickRemain = 0;
	@SerialField
	private int attackCooldown = 0;
	@SerialField
	private int blockCount = 0;
	@SerialField
	private Vec3 source = null;

	@Override
	public void onClone(Player player, boolean isWasDeath) {
		blockCount = 0;
		tickRemain = 0;
	}

	protected void startParticle(Vec3 pos) {
		tickRemain = DELAY - MARK_POS;
		source = pos;
	}

	private boolean notValid(Player player) {
		return !player.level().dimension().equals(Level.NETHER) ||
				!player.canBeSeenAsEnemy() ||
				player.hasEffect(GLEffects.UNCONSCIOUS) ||
				RolePlayHandler.transitioning(player);
	}

	@Override
	public void tick(Player player) {
		if (!GLModConfig.SERVER.koishiAttackEnable.get()) return;
		if (!(player instanceof ServerPlayer sp)) {
			if (source != null && tickRemain > 0 && tickRemain <= DELAY - MARK_POS) {
				ClientCapHandler.showParticle(player, source);
				tickRemain--;
				if (tickRemain == 0) source = null;
			}
			return;
		}
		if (tickRemain > 0) {
			tickRemain--;
			if (tickRemain == DELAY - MARK_POS) {
				source = RayTraceUtil.rayTraceBlock(player.level(), player, -2).getLocation();
				YoukaisHomecoming.HANDLER.toClientPlayer(new KoishiStartPacket(KoishiStartPacket.Type.PARTICLE, source), sp);
			}
			if (tickRemain == 0 && source != null) {
				attackCooldown = GLModConfig.SERVER.koishiAttackCoolDown.get();
				if (!notValid(player)) {
					float dmg = GLModConfig.SERVER.koishiAttackDamage.get();

					var adv = sp.server.getAdvancements().get(GLAdvGen.KOISHI_FIRST);
					if (adv != null && !sp.getAdvancements().getOrStartProgress(adv).isDone()) {
						GLCriteriaTriggers.KOISHI_FIRST.get().trigger(sp);
						dmg = Math.min(dmg, player.getMaxHealth() - 1);
					}

					if (player.hurt(GLDamageTypes.koishi(player, source), dmg)) {
						blockCount = 0;
					}
				}
				source = null;
			}
			return;
		}
		if (attackCooldown > 0) {
			attackCooldown--;
			return;
		}
		if (notValid(player)) {
			return;
		}
		if (player.getRandom().nextDouble() < GLModConfig.SERVER.koishiAttackChance.get()) {
			tickRemain = DELAY;
			GLCriteriaTriggers.KOISHI_RING.get().trigger(sp);
			YoukaisHomecoming.HANDLER.toClientPlayer(new KoishiStartPacket(KoishiStartPacket.Type.START, player.position()), sp);
		}

	}

	public void onBlock(Player player) {
		player.getCooldowns().addCooldown(player.getUseItem().getItem(), 100);
		blockCount++;
		if (blockCount >= GLModConfig.SERVER.koishiAttackBlockCount.get()) {
			blockCount = 0;
			player.spawnAtLocation(GLItems.KOISHI_HAT.get());
		}
	}

	public static void register() {
	}

}
