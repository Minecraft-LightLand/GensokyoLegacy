package dev.xkmc.gensokyolegacy.content.entity.youkai;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.api.IYoukaiEntity;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.danmakuapi.init.registrate.DanmakuEntities;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleHolder;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.*;
import dev.xkmc.gensokyolegacy.content.entity.behavior.move.YoukaiNavigationControl;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.TargetKind;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiFightEvent;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiTargetContainer;
import dev.xkmc.l2core.base.entity.SyncedData;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SerialClass
public abstract class YoukaiEntity extends DamageClampEntity implements SpellCircleHolder, IYoukaiEntity {


	private static <T> EntityDataAccessor<T> defineId(EntityDataSerializer<T> ser) {
		return SynchedEntityData.defineId(YoukaiEntity.class, ser);
	}

	protected static final SyncedData YOUKAI_DATA = new SyncedData(YoukaiEntity::defineId);

	protected static final EntityDataAccessor<Integer> DATA_FLAGS_ID = YOUKAI_DATA.define(SyncedData.INT, 0, "youkai_flags");

	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.3)
				.add(Attributes.FLYING_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 48);
	}

	@SerialField
	public final YoukaiTargetContainer targets;

	@SerialField
	public SpellCardWrapper spellCard;

	@SerialField
	protected int noTargetTime, noPlayerTime;

	protected final YoukaiSourceOverride sources = new YoukaiSourceOverride(level().registryAccess());
	protected final YoukaiCardHolder cardHolder = new YoukaiCardHolder(this);
	protected final YoukaiCombatManager combatManager = createCombatManager();
	protected final YoukaiNavigationControl navCtrl = new YoukaiNavigationControl(this);

	public YoukaiEntity(EntityType<? extends YoukaiEntity> pEntityType, Level pLevel) {
		this(pEntityType, pLevel, 10);
	}

	public YoukaiEntity(EntityType<? extends YoukaiEntity> pEntityType, Level pLevel, int maxSize) {
		super(pEntityType, pLevel);
		this.targets = new YoukaiTargetContainer(this, maxSize);
	}

	protected SoundEvent getAmbientSound() {
		return SoundEvents.EMPTY;
	}

	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.EMPTY;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.EMPTY;
	}

	// base

	protected SyncedData data() {
		return YOUKAI_DATA;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		data().register(builder);
	}

	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Age", tickCount);
		var cdc = new TagCodec(level().registryAccess());
		tag.put("auto-serial", Objects.requireNonNull(cdc.toTag(new CompoundTag(), this)));
		if (hasRestriction()) {
			var data = cdc.valueToTag(RestrictData.class, new RestrictData(getRestrictCenter(), getRestrictRadius()));
			if (data != null) tag.put("Restrict", data);
		}
		data().write(level().registryAccess(), tag, entityData);
	}

	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		tickCount = tag.getInt("Age");
		var cdc = new TagCodec(level().registryAccess());
		if (tag.contains("auto-serial")) {
			Wrappers.run(() -> cdc.fromTag(tag.getCompound("auto-serial"), getClass(), this));
		}
		var data = tag.get("Restrict");
		if (data != null) {
			RestrictData res = cdc.valueFromTag(data, RestrictData.class);
			if (res != null) {
				restrictTo(res.center(), (int) res.radius());
			}
		}
		data().read(level().registryAccess(), tag, entityData);
		if (getTarget() == null) {
			setWalking();
		}
	}

	public boolean getFlag(YoukaiFlags val) {
		int flag = 1 << val.ordinal();
		return (this.entityData.get(DATA_FLAGS_ID) & flag) != 0;
	}

	public void setFlag(YoukaiFlags val, boolean enable) {
		int b0 = this.entityData.get(DATA_FLAGS_ID);
		int flag = 1 << val.ordinal();
		if (enable) {
			b0 = (byte) (b0 | flag);
		} else {
			b0 = (byte) (b0 & (-1 - flag));
		}
		this.entityData.set(DATA_FLAGS_ID, b0);
	}

	@Override
	public void tick() {
		super.tick();
		combatManager.tick();
	}

	public void aiStep() {
		super.aiStep();
	}

	protected void customServerAiStep() {
		targets.tick();
		tickTargeting();
		tickSpell();
		navCtrl.tickMove();
		super.customServerAiStep();
	}

	// flying

	public final void setFlying() {
		navCtrl.setFlying();
	}

	public final void setWalking() {
		navCtrl.setWalking();
	}

	public void setControl(MoveControl ctrl, PathNavigation nav) {
		moveControl = ctrl;
		navigation = nav;
	}

	public final boolean isFlying() {
		return navCtrl.isFlying();
	}

	// features

	public void refreshIdle() {
		noPlayerTime = 0;
	}

	@Override
	public void handleEntityEvent(byte pId) {
		if (pId == EntityEvent.IN_LOVE_HEARTS) {
			for (int i = 0; i < 7; ++i) {
				double d0 = this.random.nextGaussian() * 0.02D;
				double d1 = this.random.nextGaussian() * 0.02D;
				double d2 = this.random.nextGaussian() * 0.02D;
				this.level().addParticle(ParticleTypes.HEART, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), d0, d1, d2);
			}
		} else super.handleEntityEvent(pId);
	}

	// combat

	protected YoukaiCombatManager createCombatManager() {
		return new DefaultCombatManager(this, null);
	}

	protected void tickTargeting() {
		if (getTarget() == null || !getTarget().isAlive()) {
			noTargetTime++;
			boolean doHeal = getFeatures().noTargetHealing() && noTargetTime >= 20 && tickCount % 20 == 0;
			doHeal |= getHealth() < getMaxHealth();
			if (doHeal && getLastHurtByMob() instanceof Player player && player.getAbilities().instabuild) {
				if (tickCount - getLastHurtByMobTimestamp() < 100) {
					doHeal = false;
				}
			}
			if (doHeal) {
				setHealth(getMaxHealth());
			}
		} else {
			noTargetTime = 0;
		}
		if (noTargetTime == 0 && getFeatures().hasBossBar()) {
			bossEvent.setVisible(true);
		}
		if (noTargetTime > 40) {
			bossEvent.setVisible(false);
		}
		if (noTargetTime > 100 && tickCount % 20 == 0) {
			var e = level().getNearestPlayer(this, 32);
			if (e == null || e.isSpectator()) {
				noPlayerTime++;
				int time = getFeatures().noPlayerDiscardTime();
				if (time > 0 && noPlayerTime > time) {
					discard();
				}
				return;
			}
		}
		noPlayerTime = 0;
	}

	protected void tickSpell() {
		if (spellCard != null) {
			if (isAggressive() && shouldShowSpellCircle()) {
				spellCard.tick(cardHolder);
			} else {
				spellCard.reset();
			}
		}
	}

	public boolean shouldIgnore(LivingEntity e) {
		if (e.getType().is(YHTagGen.YOUKAI_IGNORE))
			return true;
		if (!e.isAddedToLevel())
			return true;
		var event = new YoukaiFightEvent(this, e);
		return NeoForge.EVENT_BUS.post(event).isCanceled();
	}

	@Override
	public final boolean isInvulnerableTo(DamageSource pSource) {
		if (pSource.getEntity() instanceof LivingEntity le && shouldIgnore(le)) {
			return true;
		}
		return pSource.is(DamageTypeTags.IS_FALL) || combatManager.isInvulnerableTo(pSource);
	}

	public final TargetKind targetKind(LivingEntity le) {
		return combatManager.targetKind(le);
	}

	protected final boolean wouldAttack(LivingEntity entity) {
		if (shouldIgnore(entity)) return false;
		return targetKind(entity) == TargetKind.FIGHT;
	}

	public final boolean shouldHurt(LivingEntity le) {
		return targets.contains(le) || wouldAttack(le) || combatManager.shouldHurtInnocent(le);
	}

	public final void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		combatManager.onDanmakuHit(e, danmaku);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		if (spellCard != null) spellCard.hurt(cardHolder, source, amount);
		actuallyHurtImpl(source, amount);
	}

	@Override
	public LivingEntity self() {
		return super.self();
	}

	@Override
	public final boolean shouldShowSpellCircle() {
		return combatManager.getSpellCircle() != null;
	}

	@Override
	public final @Nullable ResourceLocation getSpellCircle() {
		return combatManager.getSpellCircle();
	}

	@Override
	public final float getCircleSize(float pTick) {
		return combatManager.getCircleSize(pTick);
	}

	public void shoot(float dmg, int life, Vec3 vec, DyeColor color) {
		ItemBulletEntity danmaku = new ItemBulletEntity(DanmakuEntities.ITEM_DANMAKU.get(), this, level());
		danmaku.setItem(DanmakuItems.Bullet.CIRCLE.get(color).asStack());
		danmaku.setup(dmg, life, true, true, vec);
		level().addFreshEntity(danmaku);
	}

}