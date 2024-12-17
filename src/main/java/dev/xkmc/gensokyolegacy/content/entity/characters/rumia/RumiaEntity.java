package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCombatManager;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiUpdateTargetTask;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.core.TaskBoard;
import dev.xkmc.gensokyolegacy.content.entity.module.AbstractYoukaiModule;
import dev.xkmc.gensokyolegacy.content.entity.module.FeedModule;
import dev.xkmc.gensokyolegacy.content.entity.module.HomeModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFlags;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowTemptation;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.ItemTemptingSensor;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.List;

@SerialClass
public class RumiaEntity extends SmartYoukaiEntity {

	private static final EntityDimensions FALL = EntityDimensions.scalable(1.7f, 0.4f);
	private static final ResourceLocation EXRUMIA = GensokyoLegacy.loc("ex_rumia");

	@SerialField
	public final RumiaStateMachine state = new RumiaStateMachine(this);

	public RumiaEntity(EntityType<? extends RumiaEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel, 8);
		setPersistenceRequired();
		sources.mobAttack = e -> level().damageSources().mobAttack(e);//TODO
	}

	@Override
	protected void constructTaskBoard(TaskBoard board) {
		super.constructTaskBoard(board);
		board.addFirst(250, new FollowTemptation<>(), Activity.IDLE, Activity.PLAY, GLBrains.AT_HOME.get());
		board.addFirst(0, new RumiaParalyzeGoal(), GLBrains.DOWN.get());

		board.addSensor(new ItemTemptingSensor<RumiaEntity>().setRadius(16, 8)
				.temptedWith((self, stack) -> stack.is(YHTagGen.DANGO)).setScanRate(e -> 20));//TODO

		board.addPrioritizedActivity(GLBrains.DOWN.get(), MemoryTest.builder(1).hasMemory(GLBrains.MEM_DOWN.get()), -100);
	}

	@SuppressWarnings({"rawtypes", "unchecked", "unsafe"})
	@Override
	public BrainActivityGroup<? extends SmartYoukaiEntity> getFightTasks() {
		return new BrainActivityGroup(Activity.FIGHT).priority(10).behaviours(
				new YoukaiUpdateTargetTask(),
				new RumiaAttackTask()
		).onlyStartWithMemoryStatus(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT);
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
		return isBlocked() ? FALL.scale(getScale()) : super.getDefaultDimensions(pPose);
	}

	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_FLAGS_ID.equals(pKey)) {
			this.refreshDimensions();
		}
	}

}