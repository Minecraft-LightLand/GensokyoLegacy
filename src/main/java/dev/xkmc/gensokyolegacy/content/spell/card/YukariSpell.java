package dev.xkmc.gensokyolegacy.content.spell.card;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.mover.RectMover;
import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.spell.part.YoukariPartLaser;
import dev.xkmc.gensokyolegacy.content.spell.part.YukariPartButterfly;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class YukariSpell extends ActualSpellCard {

	@SerialField
	private int cooldown, vulnerable;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (cooldown > 0) cooldown--;
		if (vulnerable > 0) vulnerable--;
		var target = holder.target();
		if (target == null) return;
		var pos = holder.center();
		var vel = holder.targetVelocity();
		double dist = target.distanceTo(pos);
		if (vel != null && dist > 40 && tick % 5 == 0) {
			if (vel.length() < 0.5 || vel.length() > 1) {
				var dst = target.add(target.subtract(pos).normalize().scale(32));
				if (teleport(holder.self(), dst)) {
					hidden(holder, dst, target);
					vulnerable = 5;
					return;
				}
			} else {
				var dst = target.add(target.subtract(pos).normalize().scale(-16));
				if (teleport(holder.self(), dst)) {
					hidden(holder, dst, target);
					vulnerable = 5;
					return;
				}
			}
			cooldown = 5;
			return;
		}
		if (cooldown > 0) return;
		if (dist < 20) {
			YukariPartButterfly.launchButterfly(holder, DanmakuItems.Bullet.BUTTERFLY, DyeColor.CYAN, 1);
			YukariPartButterfly.launchButterfly(holder, DanmakuItems.Bullet.BUTTERFLY, DyeColor.MAGENTA, -1);
			cooldown = 60;
			vulnerable = 20;
			return;
		}
		addTicker(new YoukariPartLaser<>());
		cooldown = 120;
		vulnerable = 40;

	}

	@Override
	public void hurt(CardHolder holder, DamageSource source, float amount) {
		if (vulnerable <= 0) {
			//TODO immune
		}
		var target = holder.target();
		if (target == null) return;
		if (teleportRandom(holder, target)) {
			hidden(holder, holder.center(), target);
		}
	}

	private void hidden(CardHolder holder, Vec3 pos, Vec3 target) {
		cooldown = 20;
		hiddenImpl(holder, pos, target);
		double dist = pos.distanceTo(target);
		Vec3 dir = pos.subtract(target).normalize();
		var ori = DanmakuHelper.getOrientation(dir);
		for (int i = 1; i < 7; i++) {
			var dst = target.add(ori.rotateDegrees(360d / 7 * i).scale(dist));
			var ans = new DelayedHidden();
			ans.pos = dst;
			ans.tick = -10 * i;
			addTicker(ans);
		}
	}

	private static void hiddenImpl(CardHolder holder, Vec3 pos, Vec3 target) {
		Vec3 dir = target.subtract(pos).normalize();
		var ori = DanmakuHelper.getOrientation(dir);
		for (int i = 0; i < 6; i++) {
			var vec = ori.rotateDegrees(60 * i);
			var laser = holder.prepareLaser(120, pos, vec, 80, DanmakuItems.Laser.LASER, DyeColor.MAGENTA);
			laser.setupTime(2, 8, 100, 10);
			holder.shoot(laser);
		}

		for (int i = 0; i < 6; i++) {
			var vec = ori.rotateDegrees(60 * i);
			var e = holder.prepareDanmaku(40, pos, DanmakuItems.Bullet.BUBBLE, DyeColor.PURPLE);
			e.mover = new RectMover(pos, vec.scale(2), Vec3.ZERO);
			holder.shoot(e);
		}

		for (int i = -3; i <= 3; i++) {
			for (int k = -2; k <= 2; k++) {
				var vec = ori.rotateDegrees(10 * i, k * 10);
				for (int j = 0; j < 3; j++) {
					var e = holder.prepareDanmaku(40, pos, DanmakuItems.Bullet.BUTTERFLY, DyeColor.PURPLE);
					e.mover = new RectMover(pos, vec.scale(1.4 + j * 0.2), Vec3.ZERO);
					holder.shoot(e);
				}
			}
		}
	}

	private static boolean teleportRandom(CardHolder holder, Vec3 target) {
		double dist = holder.center().distanceTo(target);
		var r = holder.random();
		for (int i = 0; i < 16; i++) {
			Vec3 dir = new Vec3(r.nextGaussian(), Math.abs(r.nextGaussian()), r.nextGaussian());
			Vec3 pos = dir.scale(Math.min(32, dist * (0.8 + r.nextDouble() * 0.4))).add(target);
			if (teleport(holder.self(), pos)) {
				return true;
			}
		}
		return false;
	}

	private static boolean teleport(LivingEntity mob, Vec3 target) {
		Vec3 old = mob.position();
		mob.teleportTo(target.x(), target.y(), target.z());
		if (!mob.level().noCollision(mob)) {
			mob.teleportTo(old.x(), old.y(), old.z());
			return false;
		}
		mob.level().broadcastEntityEvent(mob, EntityEvent.TELEPORT);
		mob.level().gameEvent(GameEvent.TELEPORT, mob.position(), GameEvent.Context.of(mob));
		if (!mob.isSilent()) {
			mob.level().playSound(null, mob.xo, mob.yo, mob.zo, SoundEvents.ENDERMAN_TELEPORT, mob.getSoundSource(), 1.0F, 1.0F);
			mob.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
		return true;
	}

	@SerialClass
	public static class DelayedHidden extends Ticker<YukariSpell> {

		@SerialField
		public Vec3 pos = Vec3.ZERO;

		@Override
		public boolean tick(CardHolder holder, YukariSpell card) {
			if (tick == 0) {
				var target = holder.target();
				if (target != null) {
					hiddenImpl(holder, pos, target);
				}
			}
			super.tick(holder, card);
			return tick > 0;
		}
	}

}
