package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.behavior.goals.MoveAroundNestGoal;
import dev.xkmc.gensokyolegacy.content.entity.behavior.goals.MoveRandomlyGoal;
import dev.xkmc.gensokyolegacy.content.entity.behavior.goals.MultiHurtByTargetGoal;
import dev.xkmc.gensokyolegacy.content.entity.behavior.goals.YoukaiTemptGoal;
import dev.xkmc.gensokyolegacy.content.entity.module.AbstractYoukaiModule;
import dev.xkmc.gensokyolegacy.content.entity.module.FeedModule;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFlags;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

@SerialClass
public class RumiaEntity extends YoukaiEntity {

	private static final EntityDimensions FALL = EntityDimensions.scalable(1.7f, 0.4f);
	private static final ResourceLocation EXRUMIA = GensokyoLegacy.loc("ex_rumia");

	@SerialField
	public final RumiaStateMachine state = new RumiaStateMachine(this);

	public RumiaEntity(EntityType<? extends RumiaEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		setPersistenceRequired();
		sources.mobAttack = e -> level().damageSources().mobAttack(e);//TODO
	}

	protected void registerGoals() {
		goalSelector.addGoal(3, new RumiaParalyzeGoal(this));
		goalSelector.addGoal(4, new RumiaAttackGoal(this));
		goalSelector.addGoal(5, new YoukaiTemptGoal(this, Ingredient.of(YHTagGen.DANGO)));//TODO
		goalSelector.addGoal(6, new FloatGoal(this));
		goalSelector.addGoal(6, new MoveAroundNestGoal(this, 1));
		goalSelector.addGoal(7, new MoveRandomlyGoal(this, 0.8));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 24));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new MultiHurtByTargetGoal(this, RumiaEntity.class));
		targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, this::wouldInitiateAttack));
	}

	public static AttributeSupplier.Builder createAttributes() {
		return YoukaiEntity.createAttributes()
				.add(Attributes.MAX_HEALTH, 40)
				.add(Attributes.ATTACK_DAMAGE, 6);
	}

	@Override
	public YoukaiFeatureSet getFeatures() {
		return isEx() ? YoukaiFeatureSet.BOSS : YoukaiFeatureSet.NONE;
	}

	@Override
	protected YoukaiCombatManager createCombatManager() {
		return new RumiaCombatManager(this);
	}

	@Override
	protected List<AbstractYoukaiModule> createModules() {
		return List.of(new HomeModule(this), new FeedModule(this));
	}

	@Override
	public void aiStep() {
		super.aiStep();
		state.tick();
	}

	public boolean isCharged() {
		return state != null && isAlive() && state.isCharged();
	}

	public boolean isBlocked() {
		return state != null && isAlive() && state.isBlocked();
	}

	public boolean isEx() {
		return getFlag(YoukaiFlags.POWERED);
	}

	public void setEx(boolean ex) {
		var hp = getAttribute(Attributes.MAX_HEALTH);
		var atk = getAttribute(Attributes.ATTACK_DAMAGE);
		assert hp != null && atk != null;
		if (ex) {
			hp.addPermanentModifier(new AttributeModifier(EXRUMIA, 4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			atk.addPermanentModifier(new AttributeModifier(EXRUMIA, 1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		} else {
			hp.removeModifier(EXRUMIA);
			atk.removeModifier(EXRUMIA);
		}
		setHealth(getMaxHealth());
		setFlag(YoukaiFlags.POWERED, ex);
	}

	@Override
	public void knockback(double pStrength, double pX, double pZ) {
		if (isCharged()) return;
		super.knockback(pStrength, pX, pZ);
	}

	@Override
	protected void actuallyHurt(DamageSource source, float amount) {
		boolean isVoid = source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
		if (!isVoid && !isEx() && amount >= getMaxHealth()) {
			//TODO if (YHModConfig.COMMON.exRumiaConversion.get())
			setEx(true);
		}
		if (source.getEntity() instanceof LivingEntity le) {
			state.onHurt(le, amount);
		}
		super.actuallyHurt(source, amount);
	}

	@Override
	public EntityDimensions getDefaultDimensions(Pose pPose) {
		return isBlocked() ? FALL.scale(getScale()) : super.getDimensions(pPose);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_FLAGS_ID.equals(pKey)) {
			this.refreshDimensions();
		}
	}

}