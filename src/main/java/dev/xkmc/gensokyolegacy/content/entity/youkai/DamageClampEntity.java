package dev.xkmc.gensokyolegacy.content.entity.youkai;

import com.google.common.collect.Sets;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

public class DamageClampEntity extends DamageRefactorEntity {

	protected final ServerBossEvent bossEvent = new ServerBossEvent(getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_20);

	private int hurtCD = 0;
	private boolean hurtCall = false;
	private final Set<ServerPlayer> players = Sets.newHashSet();

	protected DamageClampEntity(EntityType<? extends DamageRefactorEntity> entityType, Level level) {
		super(entityType, level);
		bossEvent.setVisible(false);
	}

	@Override
	public boolean mayBeLeashed() {
		return false;
	}

	@Override
	protected boolean canRide(Entity pVehicle) {
		return false;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	public YoukaiFeatureSet getFeatures() {
		return YoukaiFeatureSet.NONE;
	}

	public boolean shouldIgnore(LivingEntity e) {
		return false;
	}

	@Override
	public boolean canBeAffected(MobEffectInstance ins) {
		return !getFeatures().effectImmune() && super.canBeAffected(ins);
	}

	@Override
	public void tick() {
		if (hurtCD < 1000) hurtCD++;
		super.tick();
		if (!level().isClientSide()) {
			if (getFeatures().effectImmune() && !getActiveEffectsMap().isEmpty()) {
				removeAllEffects();
			}
			bossEvent.setProgress(getHealth() / getMaxHealth());
		}
	}

	private int getCD(DamageSource source) {
		//TODO config
		if (!getFeatures().damageCoolDown())
			return 10;
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
			return 10;
		if (source.getEntity() instanceof Player pl && pl.getAbilities().instabuild)
			return 10;
		if (source.is(DanmakuDamageTypes.DANMAKU_TYPE))
			return 20;
		if (source.is(DamageTypeTags.BYPASSES_COOLDOWN))
			return 40;
		return 80;
	}

	@Override
	public boolean canSwimInFluidType(FluidType type) {
		return getFeatures().effectImmune();
	}

	@Override
	public boolean fireImmune() {
		return getFeatures().effectImmune();
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return getFeatures().damageFilter() &&
				!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
				!(source.getEntity() instanceof LivingEntity) ||
				super.isInvulnerableTo(source);
	}

	protected float clampDamage(DamageSource source, float amount) {
		if (!hurtCall) return 0;
		var filter = getFeatures().damageFilter();
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			if (source.getEntity() instanceof LivingEntity le) {
				if (le instanceof ServerPlayer sp) {
					if (sp.isCreative()) {
						return amount;
					}
				}
			} else {
				if (source.is(DamageTypes.GENERIC_KILL)) {
					return amount;
				}
				if (filter && source.is(DamageTypes.FELL_OUT_OF_WORLD))
					return Math.min(4, amount);
			}
		}
		amount = Math.min(getMaxHealth() / getFeatures().limiter(), amount);
		if (!source.is(DanmakuDamageTypes.DANMAKU_TYPE))
			amount /= getFeatures().nonDanmakuProtection();
		return amount;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!source.is(DamageTypes.GENERIC_KILL) || source.getEntity() != null) {
			if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) &&
					!(source.getEntity() instanceof LivingEntity) &&
					getFeatures().damageFilter())
				return false;
			if (source.getEntity() instanceof LivingEntity le) {
				if (shouldIgnore(le)) return false;
			}
			int cd = getCD(source);
			if (getFeatures().damageCoolDown() && hurtCD < cd) {
				return false;
			}
		}

		hurtCD = 0;
		hurtCall = true;
		boolean ans = super.hurt(source, amount);
		hurtCall = false;
		return ans;
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		if (!hurtCall) return;
		super.actuallyHurt(source, amount);
	}

	@Override
	protected void hurtFinal(DamageSource source, float amount) {
		amount = clampDamage(source, amount);
		super.hurtFinal(source, amount);
	}

	@Override
	public void setHealth(float val) {
		if (level().isClientSide() || !getFeatures().damageFilter()) {
			super.setHealth(val);
		}
		float health = getHealth();
		if (tickCount > 5 && val <= health) return;
		super.setHealth(val);
	}

	public void heal(float original) {
		if (!getFeatures().effectImmune()) {
			super.heal(original);
			return;
		}
		var heal = EventHooks.onLivingHeal(this, original);
		heal = Math.max(original, heal);
		if (heal <= 0) return;
		float f = getHealth();
		if (f > 0) {
			setHealth(f + heal);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		var name = getDisplayName();
		if (hasCustomName() && name != null) {
			bossEvent.setName(name);
		}
	}

	public void setCustomName(@Nullable Component pName) {
		super.setCustomName(pName);
		if (pName != null) bossEvent.setName(pName);
	}

	public void startSeenByPlayer(ServerPlayer pPlayer) {
		super.startSeenByPlayer(pPlayer);
		players.add(pPlayer);
		this.bossEvent.addPlayer(pPlayer);
	}

	public void stopSeenByPlayer(ServerPlayer pPlayer) {
		super.stopSeenByPlayer(pPlayer);
		players.remove(pPlayer);
		this.bossEvent.removePlayer(pPlayer);
	}

	public Collection<ServerPlayer> getPlayers() {
		return players;
	}

}
