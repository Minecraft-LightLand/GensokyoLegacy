package dev.xkmc.gensokyolegacy.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.gensokyolegacy.content.entity.characters.maiden.MaidenEntity;
import dev.xkmc.gensokyolegacy.init.data.GLAdvGen;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.GLModConfig;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.GolemSensor;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ReimuEventHandlers {

	private static List<Villager> getWitness(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return List.of();
		AABB aabb = le.getBoundingBox().inflate(range);
		return sl.getEntities(EntityTypeTest.forClass(Villager.class), aabb,
				e -> e.isAlive() && !e.hasEffect(YHEffects.HYPNOSIS)
						&& (!eatFlesh || e.hasLineOfSight(le)));
	}

	public static void triggerReimuResponse(LivingEntity le, int range, boolean eatFlesh) {
		if (!(le.level() instanceof ServerLevel sl)) return;
		var list = getWitness(le, range, eatFlesh);
		if (!list.isEmpty()) {
			if (le instanceof ServerPlayer sp && eatFlesh) {
				if (!GLModConfig.SERVER.reimuSummonFlesh.get() || fleshWarn(sp)) {
					for (var e : list) {
						sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
						sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
					}
					return;
				}
			}
			if (trySummonReimuAttack(sl, le)) {
				list.forEach(GolemSensor::golemDetected);
			}
		}
		for (var e : list) {
			sl.broadcastEntityEvent(e, EntityEvent.VILLAGER_ANGRY);
			sl.onReputationEvent(ReputationEventType.VILLAGER_KILLED, le, e);
		}
	}

	public static boolean koishiBlockReimu(LivingEntity le) {
		var hat = GLItems.KOISHI_HAT.get();
		if (le instanceof ServerPlayer sp &&
				le.getItemBySlot(EquipmentSlot.HEAD).is(hat) &&
				!sp.getCooldowns().isOnCooldown(hat)) {
			KoishiEventHandlers.removeKoishi(le);
			sp.getCooldowns().addCooldown(hat, 1200);
			sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
			sp.sendSystemMessage(GLLang.MSG$KOISHI_REIMU.get(), true);
			return true;
		}
		return false;
	}

	public static boolean fleshWarn(ServerPlayer sp) {
		if (koishiBlockReimu(sp)) return true;
		var adv = sp.server.getAdvancements().get(GLAdvGen.FLESH_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return false;
		GLCriteriaTriggers.FLESH_WARN.get().trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(GLLang.MSG$REIMU_FLESH.get(), true);
		return true;
	}

	public static void hurtWarn(ServerPlayer sp) {
		if (getWitness(sp, 16, false).isEmpty()) return;
		if (koishiBlockReimu(sp)) return;
		var adv = sp.server.getAdvancements().get(GLAdvGen.HURT_WARN);
		if (adv == null || sp.getAdvancements().getOrStartProgress(adv).isDone()) return;
		GLCriteriaTriggers.HURT_WARN.get().trigger(sp);
		sp.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100));
		sp.sendSystemMessage(GLLang.MSG$REIMU_WARN.get(), true);
	}

	@Nullable
	public static MaidenEntity trySummonReimu(ServerLevel sl, LivingEntity le) {
		BlockPos center = BlockPos.containing(le.position().add(le.getForward().scale(8)).add(0, 5, 0));
		EntityEntry<? extends MaidenEntity> type = GLEntities.REIMU;
		if (ModList.get().isLoaded(TouhouLittleMaid.MOD_ID)) {
			if (le.getRandom().nextFloat() > 0.5) {
				if (le.getRandom().nextFloat() > 0.5) {
					type = GLEntities.SANAE;
				} else {
					type = GLEntities.MARISA;
				}
			}
		}
		MaidenEntity e = type.create(sl);
		if (e == null) return null;
		BlockPos pos = getPosForReimuSpawn(le, e, center, 16, 8, 5);
		if (pos == null) {
			center = le.blockPosition().above(5);
			pos = getPosForReimuSpawn(le, e, center, 16, 16, 5);
		}
		if (pos == null) return null;
		e.moveTo(pos, 0, 0);
		e.initSpellCard();
		sl.addFreshEntity(e);
		return e;
	}

	private static boolean trySummonReimuAttack(ServerLevel sl, LivingEntity le) {
		if (le instanceof ServerPlayer sp && sp.isCreative()) return false;
		if (koishiBlockReimu(le)) return false;
		KoishiEventHandlers.removeKoishi(le);
		var list = sl.getEntities(EntityTypeTest.forClass(MaidenEntity.class),
				le.getBoundingBox().inflate(32), LivingEntity::isAlive);
		if (!list.isEmpty()) {
			for (var e : list) {
				e.setTarget(le);
				e.refreshIdle();
			}
			return false;
		}
		var maiden = trySummonReimu(sl, le);
		if (maiden == null) return false;
		KoishiEventHandlers.removeKoishi(le);
		maiden.setTarget(le);
		return true;
	}

	@Nullable
	private static BlockPos getPosForReimuSpawn(LivingEntity sp, Entity e, BlockPos center, int trial, int range, int dy) {
		for (int i = 0; i < trial; i++) {
			BlockPos pos = center.offset(
					sp.getRandom().nextInt(-range, range),
					sp.getRandom().nextInt(-dy, dy),
					sp.getRandom().nextInt(-range, range)
			);
			e.moveTo(pos, 0, 0);
			if (sp.level().noCollision(e)) {
				return pos;
			}
		}
		return null;
	}

}
