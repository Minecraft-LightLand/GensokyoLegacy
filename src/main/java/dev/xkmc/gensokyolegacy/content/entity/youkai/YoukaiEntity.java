package dev.xkmc.gensokyolegacy.content.entity.youkai;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.api.IYoukaiEntity;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.spell.spellcard.SpellCardWrapper;
import dev.xkmc.danmakuapi.init.registrate.DanmakuEntities;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.fastprojectileapi.spellcircle.SpellCircleHolder;
import dev.xkmc.gensokyolegacy.content.attachment.character.CharDataHolder;
import dev.xkmc.gensokyolegacy.content.attachment.character.ReputationState;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.*;
import dev.xkmc.gensokyolegacy.content.entity.behavior.move.YoukaiNavigationControl;
import dev.xkmc.gensokyolegacy.content.entity.module.*;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2core.base.entity.SyncedData;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
	protected final YoukaiModuleHolder modules = new YoukaiModuleHolder(createModules());

	public final YoukaiCardHolder cardHolder = new YoukaiCardHolder(this);
	public final YoukaiCombatManager combatManager = createCombatManager();
	public final YoukaiNavigationControl navCtrl = new YoukaiNavigationControl(this);

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
		var moduleTag = new CompoundTag();
		for (var e : modules) {
			var val = cdc.toTag(new CompoundTag(), e);
			if (val != null) {
				moduleTag.put(e.getId().toString(), val);
			}
		}
		tag.put("YoukaiModules", moduleTag);
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
		if (tag.contains("YoukaiModules")) {
			var moduleTag = tag.getCompound("YoukaiModules");
			for (var e : modules) {
				if (!moduleTag.contains(e.getId().toString())) continue;
				var val = moduleTag.getCompound(e.getId().toString());
				cdc.fromTag(val, e.getClass(), e);
			}
		}
		if (getTarget() == null) {
			navCtrl.setWalking();
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

	// features

	@Override
	public void tick() {
		super.tick();
		combatManager.tick();
	}

	protected void customServerAiStep() {
		targets.tick();
		tickTargeting();
		tickSpell();
		navCtrl.tickMove();
		for (var e : modules) {
			e.tickServer();
		}
		super.customServerAiStep();
	}

	public void setControl(MoveControl ctrl, PathNavigation nav) {
		moveControl = ctrl;
		navigation = nav;
	}

	protected List<AbstractYoukaiModule> createModules() {
		return List.of(new HomeModule(this), new FeedModule(this), new TalkModule(this));
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		for (var e : modules) {
			InteractionResult result = e.interact(player, hand);
			if (result != InteractionResult.PASS)
				return result;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public void handleEntityEvent(byte pId) {
		for (var e : modules) {
			if (e.handleEntityEvent(pId))
				return;
		}
		super.handleEntityEvent(pId);
	}

	@Override
	protected void tickEffects() {
		super.tickEffects();
		for (var e : modules) {
			e.tickClient();
		}
	}

	@Override
	public boolean canPickUpLoot() {
		return tickCount % 10 == 0 && modules.hasPickup();
	}

	@Override
	public boolean wantsToPickUp(ItemStack stack) {
		for (var e : modules) {
			if (e instanceof IPickupModule pick) {
				if (pick.canPickup(stack)) {
					return true;
				}
			}
		}
		return super.wantsToPickUp(stack);
	}

	@Override
	protected void pickUpItem(ItemEntity item) {
		for (var e : modules) {
			if (e instanceof IPickupModule pick) {
				if (pick.canPickup(item.getItem())) {
					pick.onPickup(item);
					if (item.isRemoved()) {
						return;
					}
				}
			}
		}
		super.pickUpItem(item);
	}

	// data

	public Optional<CharDataHolder> getData(@Nullable Entity e) {
		if (e instanceof Player player) {
			return Optional.ofNullable(GLMisc.CHAR.type().getOrCreate(player).get(player, this));
		}
		return Optional.empty();
	}

	public <T> Optional<T> getModule(Class<T> cls) {
		return modules.getModule(cls);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		if (spellCard != null) spellCard.hurt(cardHolder, source, amount);
		getData(source.getEntity()).ifPresent(e -> e.onHurt(source, amount));
		actuallyHurtImpl(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		boolean prev = dead;
		super.die(source);
		for (var module : modules) {
			module.onKilled();
		}
		var e = source.getEntity();
		if (!prev && dead && e instanceof LivingEntity le) {
			if (!e.isAlive() || !e.isAddedToLevel() || e.isRemoved())
				return;
			onKilledBy(le, source);
		}
	}

	@Override
	public boolean killedEntity(ServerLevel level, LivingEntity entity) {
		getData(entity).ifPresent(CharDataHolder::onKilledByCharacter);
		return true;
	}

	protected void onKilledBy(LivingEntity le, DamageSource source) {
		getData(le).ifPresent(CharDataHolder::onKillCharacter);
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
		if (e instanceof Player player && player.getAbilities().instabuild)
			return false;
		if (!targets.isValidTarget(e))
			return true;
		var event = new YoukaiFightEvent(this, e);
		return NeoForge.EVENT_BUS.post(event).isCanceled();
	}

	@Override
	public final boolean isInvulnerableTo(DamageSource pSource) {
		if (pSource.getEntity() instanceof LivingEntity le && shouldIgnore(le)) {
			return true;
		}
		return pSource.is(DamageTypeTags.IS_FALL) ||
				combatManager.isInvulnerableTo(pSource) ||
				super.isInvulnerableTo(pSource);
	}

	public final boolean wouldInitiateAttack(LivingEntity entity) {
		if (!targets.isValidTarget(entity)) return false;
		if (shouldIgnore(entity)) return false;
		return combatManager.targetKind(entity).initiateAttack();
	}

	public final boolean isHostileTo(LivingEntity le) {
		return targets.contains(le) || wouldInitiateAttack(le);
	}

	public final boolean shouldHurt(LivingEntity le) {
		if (shouldIgnore(le)) return false;
		return isHostileTo(le) || combatManager.targetKind(le).isPrey() || combatManager.shouldHurtInnocent(le);
	}

	public final void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		combatManager.onDanmakuHit(e, danmaku);
	}

	@Override
	public void onDanmakuImmune(LivingEntity e, IDanmakuEntity danmaku, DamageSource source) {
		combatManager.onDanmakuImmune(e, danmaku, source);
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

	// misc

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData) {
		initSpellCard();
		return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData);
	}

	public void initSpellCard() {
	}

	public void startSleeping(BlockPos pos) {
		if (this.isPassenger()) {
			this.stopRiding();
		}
		this.setPose(Pose.SLEEPING);
		this.setPos(pos.getX() + 0.5, pos.getY() + 0.6875, pos.getZ() + 0.5);
		this.setSleepingPos(pos);
		this.setDeltaMovement(Vec3.ZERO);
		this.hasImpulse = true;
	}

	public boolean mayInteract(Player player) {
		return !isHostileTo(player) && getModule(TalkModule.class).map(e -> !e.isTalking()).orElse(true);
	}

	public ReputationState getReputation(LivingEntity le) {
		return getData(le).map(e -> e.data().getState()).orElse(ReputationState.STRANGER);
	}

	public void setTalkTo(@Nullable ServerPlayer player, int time) {
		if (player == null) return;
		getNavigation().stop();
		lookAt(player, 30, 30);
	}

}