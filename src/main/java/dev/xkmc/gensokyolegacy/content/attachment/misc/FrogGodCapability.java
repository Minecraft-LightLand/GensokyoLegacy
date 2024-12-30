package dev.xkmc.gensokyolegacy.content.attachment.misc;

import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.capability.attachment.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

import java.util.LinkedHashSet;

@SerialClass
public class FrogGodCapability extends GeneralCapabilityTemplate<Frog, FrogGodCapability> {

	@SerialField
	public boolean hasHat;

	@SerialField
	private final LinkedHashSet<EntityType<?>> eaten = new LinkedHashSet<>();

	public static void register() {
	}

	public void syncToClient(LivingEntity entity) {
		YoukaisHomecoming.HANDLER.toTrackingPlayers(new FrogSyncPacket(entity, this), entity);
	}

	public void eat(Frog frog, Entity other) {
		if (!hasHat) return;
		var type = other.getType();
		if (!type.is(EntityTypeTags.RAIDERS)) return;
		int noSight = GLModConfig.SERVER.frogEatRaiderVillagerNoSightRange.get();
		int sight = GLModConfig.SERVER.frogEatRaiderVillagerSightRange.get();
		AABB aabb = frog.getBoundingBox().inflate(noSight);
		var list = frog.level().getEntities(EntityTypeTest.forClass(Villager.class), aabb, e ->
				e.hasLineOfSight(frog) || e.hasLineOfSight(other) || e.distanceTo(frog) < sight);
		if (list.isEmpty()) return;
		eaten.add(type);
		if (eaten.size() >= GLModConfig.SERVER.frogEatCountForHat.get()) {
			eaten.clear();
			hasHat = false;
			syncToClient(frog);
			frog.spawnAtLocation(GLItems.SUWAKO_HAT.get());
		}
	}

	public static boolean canEatSpecial(Frog frog, LivingEntity target) {
		if (GLMeta.FROG_GOD.type().isProper(frog)) {
			FrogGodCapability cap = GLMeta.FROG_GOD.type().getOrCreate(frog);
			return cap.hasHat && target.getType().is(EntityTypeTags.RAIDERS);
		}
		return false;
	}

	public static boolean hasHat(Frog frog) {
		if (GLMeta.FROG_GOD.type().isProper(frog)) {
			FrogGodCapability cap = GLMeta.FROG_GOD.type().getOrCreate(frog);
			return cap.hasHat;
		}
		return false;
	}

	public static void onEatTarget(Frog frog, Entity instance) {
		if (GLMeta.FROG_GOD.type().isProper(frog)) {
			FrogGodCapability cap = GLMeta.FROG_GOD.type().getOrCreate(frog);
			cap.eat(frog, instance);
		}
	}

	public static void startTracking(Frog frog, Player entity) {
		if (GLMeta.FROG_GOD.type().isProper(frog)) {
			FrogGodCapability cap = GLMeta.FROG_GOD.type().getOrCreate(frog);
			YoukaisHomecoming.HANDLER.toClientPlayer(new FrogSyncPacket(frog, cap), (ServerPlayer) entity);
		}
	}

}
