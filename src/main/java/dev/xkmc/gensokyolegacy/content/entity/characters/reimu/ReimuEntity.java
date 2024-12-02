package dev.xkmc.gensokyolegacy.content.entity.characters.reimu;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.compat.touhoulittlemaid.TouhouSpellCards;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiFeatureSet;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import dev.xkmc.youkaishomecoming.init.data.YHTagGen;
import dev.xkmc.youkaishomecoming.init.registrate.YHEffects;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.UUID;

@SerialClass
public class ReimuEntity extends MaidenEntity {

	@SerialField
	private final HashSet<UUID> verifiedPlayers = new HashSet<>();
	@SerialField
	private int feedCD = 0;

	public ReimuEntity(EntityType<? extends ReimuEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
	}

	@Override
	public YoukaiFeatureSet getFeatures() {
		return YoukaiFeatureSet.MAIDEN;
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();
		if (feedCD > 0) feedCD--;
		setFlag(16, feedCD > 0);
	}

	@Override
	protected void tickEffects() {
		super.tickEffects();
		if (getFlag(16)) {
			if (isInvisible() ? random.nextInt(15) == 0 : random.nextBoolean()) {
				level().addParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, MobEffects.SATURATION.value().getColor()),
						getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 0, 0, 0);
			}
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var food = stack.getFoodProperties(this);
		if (food != null) {
			if (player instanceof ServerPlayer sp && feedCD == 0) {
				if (stack.is(YHTagGen.FLESH_FOOD)) {
					setTarget(player);
					return InteractionResult.SUCCESS;
				}
				ItemStack remain = stack.getCraftingRemainingItem();
				feedTrigger(sp, stack);
				stack.shrink(1);
				feedCD += food.nutrition() * 100;
				if (food.effects().stream().anyMatch(e -> e.effect().getEffect() == YHEffects.UDUMBARA.get())) {
					verifiedPlayers.add(sp.getUUID());
					//TODO YHCriteriaTriggers.REIMU_HAPPY.trigger(sp);
				}
				if (stack.getUseAnimation() == UseAnim.DRINK)
					playSound(stack.getDrinkingSound());
				else playSound(stack.getEatingSound());
				if (!remain.isEmpty())
					sp.getInventory().placeItemBackInInventory(remain);
			}
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	private void feedTrigger(ServerPlayer sp, ItemStack stack) {
		var sv = sp.getServer();
		if (sv == null) return;
		var e = sv.getAdvancements().get(YoukaisHomecoming.loc("main/feed_reimu"));
		if (e == null) return;
		var prog = sp.getAdvancements().getOrStartProgress(e);
		float count = prog.getPercent();
		//TODO YHCriteriaTriggers.FEED_REIMU.trigger(sp, stack);
		if (prog.getPercent() > count) {
			level().broadcastEntityEvent(this, EntityEvent.IN_LOVE_HEARTS);
		}
	}

	@Override
	public void initSpellCard() {
		TouhouSpellCards.setReimu(this);
	}

	@Override
	public void die(DamageSource source) {
		boolean prev = dead;
		super.die(source);
		var e = source.getEntity();
		if (!prev && dead && e instanceof LivingEntity && !source.is(DanmakuDamageTypes.DANMAKU_TYPE)) {
			if (!e.isAlive() || !e.isAddedToLevel() || e.isRemoved())
				return;
			//TODO TouhouConditionalSpawns.triggetYukari(le, position());
		}
	}

	/* TODO
	@Override
	public void onDanmakuImmune(LivingEntity e, IDanmakuEntity danmaku, DamageSource source) {
		if (e.tickCount - e.getLastHurtByMobTimestamp() < 20)
			return;
		if (e instanceof Player player && player.getAbilities().instabuild)
			return;
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS))
			return;
		double rate = e instanceof Player ?
				YHModConfig.COMMON.danmakuPlayerPHPDamage.get() :
				YHModConfig.COMMON.danmakuMinPHPDamage.get();
		double dmg = Math.max(rate * Math.max(e.getHealth(), e.getMaxHealth()), danmaku.damage(e));
		e.setHealth(e.getHealth() - (float) dmg);
		if (e.isDeadOrDying()) {
			e.die(DanmakuDamageTypes.abyssal(danmaku));
		}
	}*/

}
