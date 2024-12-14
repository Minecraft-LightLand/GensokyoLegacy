package dev.xkmc.gensokyolegacy.content.entity.characters.fairy;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.TargetKind;
import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.YoukaiCardHolder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.DyeColor;

class CirnoCombatManager extends FairyCombatManager {

	public CirnoCombatManager(CirnoEntity cirnoEntity) {
		super(cirnoEntity);
	}

	@Override
	public TargetKind targetKind(LivingEntity le) {
		if (le instanceof Frog) return TargetKind.PRAY;
		return super.targetKind(le);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source.is(DamageTypeTags.IS_FREEZING);
	}

	@Override
	public void onDanmakuHit(LivingEntity e, IDanmakuEntity danmaku) {
		if (targetKind(e).noAdditionalEffect()) return;
		e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
		if (e.canFreeze()) {
			e.setTicksFrozen(Math.min(200, e.getTicksFrozen() + 120));
		}
	}

	@Override
	public int doPreyAttack(LivingEntity target) {
		var holder = new YoukaiCardHolder(self);
		holder.shoot(holder.prepareDanmaku(40, holder.forward(), DanmakuItems.Bullet.CIRCLE, DyeColor.LIGHT_BLUE));
		return 10;
	}

}
