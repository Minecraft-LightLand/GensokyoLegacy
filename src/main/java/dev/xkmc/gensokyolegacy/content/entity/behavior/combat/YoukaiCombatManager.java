package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface YoukaiCombatManager {

	boolean isInvulnerableTo(DamageSource source);

	TargetKind targetKind(LivingEntity le);

	boolean shouldHurtInnocent(LivingEntity le);

	int doPreyAttack(LivingEntity target);

	void tick();

	/*

		@Nullable
		Identifier getSpellCircle();

		float getCircleSize(float pTick);

		void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku);

		void onDanmakuImmune(LivingEntity e, IDanmakuEntity danmaku, DamageSource source);

	 */

}
