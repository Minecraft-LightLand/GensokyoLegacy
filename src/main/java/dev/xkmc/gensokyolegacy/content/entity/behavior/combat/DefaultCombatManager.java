package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class DefaultCombatManager implements YoukaiCombatManager {

	protected final YoukaiEntity self;
	@Nullable
	protected final ResourceLocation circle;

	private int tickAggressive;

	public DefaultCombatManager(YoukaiEntity self, @Nullable ResourceLocation circle) {
		this.self = self;
		this.circle = circle;
	}

	@Override
	public void tick() {
		if (self.level().isClientSide()) {
			if (shouldShowCircle()) {
				if (tickAggressive < 20)
					tickAggressive++;
			} else if (tickAggressive > 0) {
				tickAggressive--;
			}
		}
	}

	public boolean shouldShowCircle() {
		return self.isAggressive();
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return false;
	}

	@Override
	public TargetKind targetKind(LivingEntity le) {
		if (le instanceof YoukaiEntity) return TargetKind.WORTHY;
		return self.getReputation(le).asTargetKind();
	}

	@Override
	public boolean shouldHurtInnocent(LivingEntity le) {
		return le instanceof Enemy;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		if (targetKind(e).noAdditionalEffect()) return;
		if (self.targets.contains(e)) {
			double heal = 0.2;//TODO YHModConfig.COMMON.danmakuHealOnHitTarget.get();
			self.heal(self.getMaxHealth() * (float) heal);
		}
	}

	@Override
	public void onDanmakuImmune(LivingEntity e, IDanmakuEntity danmaku, DamageSource source) {
		if (!self.getFeatures().trueDamageOnImmune()) return;
		if (e.tickCount - e.getLastHurtByMobTimestamp() < 20) return;
		if (e instanceof Player player && player.getAbilities().instabuild) return;
		if (!source.is(DamageTypeTags.BYPASSES_EFFECTS)) return;
		double rate = 0.2;// TODO e instanceof Player ? YHModConfig.COMMON.danmakuPlayerPHPDamage.get() : YHModConfig.COMMON.danmakuMinPHPDamage.get();
		double dmg = Math.max(rate * Math.max(e.getHealth(), e.getMaxHealth()), danmaku.damage(e));
		e.setHealth(e.getHealth() - (float) dmg);
		if (e.isDeadOrDying())
			e.die(source);
	}

	@Override
	public @Nullable ResourceLocation getSpellCircle() {
		if (!shouldShowCircle()) {
			return null;
		}
		return circle;
	}

	@Override
	public float getCircleSize(float pTick) {
		return tickAggressive == 0 ? 0 : Math.min(1, (tickAggressive + pTick) / 20f);
	}

	@Override
	public int doPreyAttack(LivingEntity target) {
		return 20;
	}

}
