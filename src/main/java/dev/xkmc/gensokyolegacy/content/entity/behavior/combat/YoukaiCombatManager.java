package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface YoukaiCombatManager {

	boolean isInvulnerableTo(DamageSource source);

	TargetKind targetKind(LivingEntity le);

	boolean shouldHurtInnocent(LivingEntity le);

	@Nullable
	ResourceLocation getSpellCircle();

	float getCircleSize(float pTick);

	void tick();

	void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku);

	void onDanmakuImmune(LivingEntity e, IDanmakuEntity danmaku, DamageSource source);

	int doPreyAttack(LivingEntity target);

}
