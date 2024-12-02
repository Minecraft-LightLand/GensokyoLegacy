package dev.xkmc.gensokyolegacy.content.entity.youkai;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.damagesource.DamageContainer;

public class DamageRefactorEntity extends PathfinderMob {

	protected DamageRefactorEntity(EntityType<? extends DamageRefactorEntity> entityType, Level level) {
		super(entityType, level);
	}

	protected final void actuallyHurtImpl(DamageSource source, float amount) {
		if (!this.isInvulnerableTo(source)) {
			var cont = damageContainers;
			cont.peek().setReduction(DamageContainer.Reduction.ARMOR,
					cont.peek().getNewDamage() - this.getDamageAfterArmorAbsorb(source, cont.peek().getNewDamage()));
			this.getDamageAfterMagicAbsorb(source, cont.peek().getNewDamage());
			float damage = CommonHooks.onLivingDamagePre(this, cont.peek());
			hurtFinal(source, damage);
			CommonHooks.onLivingDamagePost(this, cont.peek());
		}
	}

	protected void hurtFinal(DamageSource source, float damage) {
		var cont = damageContainers;
		cont.peek().setReduction(DamageContainer.Reduction.ABSORPTION, Math.min(this.getAbsorptionAmount(), damage));
		float absorbed = Math.min(damage, (cont.peek()).getReduction(DamageContainer.Reduction.ABSORPTION));
		this.setAbsorptionAmount(Math.max(0.0F, this.getAbsorptionAmount() - absorbed));
		float f1 = cont.peek().getNewDamage();
		if (absorbed > 0.0F && absorbed < 3.4028235E37F) {
			Entity var8 = source.getEntity();
			if (var8 instanceof ServerPlayer serverplayer) {
				serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbed * 10.0F));
			}
		}
		if (f1 != 0.0F) {
			this.getCombatTracker().recordDamage(source, f1);
			hurtFinalImpl(source, this.getHealth() - f1);
			this.gameEvent(GameEvent.ENTITY_DAMAGE);
			this.onDamageTaken(cont.peek());
		}
	}

	protected void hurtFinalImpl(DamageSource source, float amount) {
		super.setHealth(amount);
	}

}
