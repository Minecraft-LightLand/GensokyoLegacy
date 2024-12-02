package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
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
		//TODO fight
		return TargetKind.NONE;
	}

	@Override
	public boolean shouldHurtInnocent(LivingEntity le) {
		return le instanceof Enemy;
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		if (targetKind(e) == TargetKind.WORTHY) return;
		if (self.targets.contains(e)) {
			double heal = 0.2;//TODO YHModConfig.COMMON.danmakuHealOnHitTarget.get();
			self.heal(self.getMaxHealth() * (float) heal);
		}
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

}
