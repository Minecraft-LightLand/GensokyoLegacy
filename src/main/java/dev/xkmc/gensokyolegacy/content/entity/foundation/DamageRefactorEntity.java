package dev.xkmc.gensokyolegacy.content.entity.foundation;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class DamageRefactorEntity extends PathfinderMob {

	@SerialField
	private CombatProgress combatProgress = new CombatProgress();

	protected DamageRefactorEntity(EntityType<? extends DamageRefactorEntity> entityType, Level level) {
		super(entityType, level);
		combatProgress.init(this);
	}

	public boolean invalidTarget(LivingEntity e) {
		return e.isRemoved() || !e.isAlive() || !e.isAddedToLevel() || e.level() != level();
	}

	@Nullable
	@Override
	public LivingEntity getTarget() {
		LivingEntity ans = super.getTarget();
		if (ans == null || invalidTarget(ans)) return null;
		return ans;
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
		cont.peek().setReduction(DamageContainer.Reduction.ABSORPTION, Math.min(Math.max(0, getAbsorptionAmount()), damage));
		float absorbed = Math.min(damage, (cont.peek()).getReduction(DamageContainer.Reduction.ABSORPTION));
		this.setAbsorptionAmount(Math.max(0.0F, Math.max(0, getAbsorptionAmount()) - absorbed));
		float actual = cont.peek().getNewDamage();
		if (absorbed > 0.0F && absorbed < 3.4028235E37F) {
			Entity var8 = source.getEntity();
			if (var8 instanceof ServerPlayer serverplayer) {
				serverplayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbed * 10.0F));
			}
		}
		if (actual != 0.0F) {
			this.getCombatTracker().recordDamage(source, actual);
			hurtFinalImpl(source, actual);
			this.gameEvent(GameEvent.ENTITY_DAMAGE);
			this.onDamageTaken(cont.peek());
		}
	}

	protected void hurtFinalImpl(DamageSource source, float amount) {
		if (combatProgress == null) return;
		if (!Float.isFinite(amount)) return;
		if (amount < 0) return;
		setCombatProgress(getCombatProgress() - amount);
	}

	public void validateData() {
		if (getCombatProgress() > 0) {
			if (deathTime > 0) deathTime = 0;
			if (dead) dead = false;
		}
	}

	@Override
	public float getHealth() {
		return Math.min(getCombatProgress(), getMaxHealth());
	}

	@Override
	public void setHealth(float amount) {
		if (!Float.isFinite(amount)) return;
		setCombatProgress(amount);
	}

	public void setCombatProgress(float amount) {
		super.setHealth(amount);
		if (combatProgress == null) return;
		if (amount > getMaxHealth()) {
			combatProgress.setMax(getMaxHealth());
		} else {
			combatProgress.set(this, amount);
		}
	}

	public float getCombatProgress() {
		return combatProgress == null ? super.getHealth() : combatProgress.getProgress();
	}

	// guards

	@Override
	protected boolean isImmobile() {
		return this.getCombatProgress() <= 0.0F;
	}

	@Override
	public boolean isDeadOrDying() {
		return this.getCombatProgress() <= 0.0F;
	}

	public boolean isAlive() {
		return !this.isRemoved() && this.getCombatProgress() > 0.0F;
	}

	@Override
	protected void tickDeath() {
		if (getCombatProgress() > 0) return;
		super.tickDeath();
	}

	@Override
	public void die(DamageSource source) {
		if (getCombatProgress() > 0) return;
		super.die(source);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putFloat("Health", getCombatProgress());
	}

}
