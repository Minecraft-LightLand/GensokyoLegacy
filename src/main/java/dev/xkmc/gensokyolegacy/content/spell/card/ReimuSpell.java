package dev.xkmc.gensokyolegacy.content.spell.card;

import dev.xkmc.danmakuapi.api.IDanmakuEntity;
import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.entity.ItemBulletEntity;
import dev.xkmc.danmakuapi.content.item.DanmakuItem;
import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.gensokyolegacy.content.spell.part.ReimuPart;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class ReimuSpell extends ActualSpellCard {

	@SerialField
	private boolean border, abyss;

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		int interval = 10;
		var target = holder.target();
		var dist = target == null ? 0 : holder.center().distanceTo(target);
		boolean far = dist > 40;
		if (tick % interval == 0) {
			int step = tick / interval % 5;
			if (step < 3) {
				shoot(far);
			} else if (dist > 40) {
				intercept(holder, target);
			}
		}
		if (target != null && border)
			border(holder, dist);
	}

	private void shoot(boolean far) {
		ReimuPart<ReimuSpell> ans = new ReimuPart<>();
		if (far) ans.setRad(32, 32, 10, 10, 3);
		else ans.setRad(8, 6, 20, 20, 1);
		addTicker(ans.setVals(20, 30, 40, 60)
				.setProp(DanmakuItems.Bullet.CIRCLE, abyss ? DyeColor.BLUE : DyeColor.RED));
	}

	private void intercept(CardHolder holder, Vec3 target) {
		var dir = holder.targetVelocity();
		if (dir == null) return;
		double speed = dir.length();
		dir = dir.normalize();
		if (dir.length() < 0.1) return;
		double dist = Math.max(24, speed * 20);
		var dst = target.add(dir.scale(dist));
		if (dst.y < holder.self().level().getMinBuildHeight()) {
			dst = new Vec3(dst.x, target.y, dst.z);
		}
		if (!teleport(holder.self(), dst)) return;
		if (speed < 0.5) return;
		Intercept ans = new Intercept();
		ans.vec = target;
		ans.dir = dir;
		addTicker(ans);
	}

	private void border(CardHolder holder, double dist) {
		var forward = holder.forward();
		var ori = DanmakuHelper.getOrientation(forward);
		double angle = ProjectileMovement.of(forward).rot().y * Mth.RAD_TO_DEG;
		double speed = Mth.clamp(dist / 30, 1.5, 3);
		for (int i = 0; i < 8; i++) {
			var dir = ori.rotateDegrees(360d / 8 * i - angle);
			var e = holder.prepareDanmaku(40, dir.scale(speed), DanmakuItems.Bullet.BALL, DyeColor.YELLOW);
			holder.shoot(e);
		}
	}

	@Override
	public DamageSource getDanmakuDamageSource(IDanmakuEntity danmaku) {
		if (danmaku instanceof ItemBulletEntity e) {
			if (e.getItem().getItem() instanceof DanmakuItem i) {
				if (i.color == DyeColor.BLUE || i.color == DyeColor.YELLOW) {
					return DanmakuDamageTypes.abyssal(danmaku);
				}
			}
		}
		return super.getDanmakuDamageSource(danmaku);
	}

	@Override
	public void hurt(CardHolder holder, DamageSource source, float amount) {
		super.hurt(holder, source, amount);
		if (source.getEntity() != null) border = true;
		float hp = holder.self().getHealth(), mhp = holder.self().getMaxHealth();
		if (hp < mhp / 2) {
			abyss = true;
		}
		var target = holder.target();
		var dist = target == null ? 0 : holder.center().distanceTo(target);
		homingReact(holder, dist > 40);
	}

	private void homingReact(CardHolder holder, boolean far) {
		var le = holder.target();
		if (le == null) return;
		var r = holder.random();
		var dir = le.subtract(holder.center()).normalize();
		if (r.nextDouble() < 0.5) dir = new Vec3(1, 0, 0);
		var ori = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 60 * r.nextDouble() - 30);
		var normal = dir.cross(ori);
		int n = 8;
		int s = r.nextDouble() < 0.5 ? -1 : 1;
		for (int i = 0; i <= 5; i++) {
			var init = DanmakuHelper.getOrientation(ori, normal).rotateDegrees(s * (i - 2) * 360d / n / 4);
			ReimuPart<ReimuSpell> ans = new ReimuPart<>();
			if (far) ans.setRad(32, 32, 10, 10, 3);
			else ans.setRad(24, 18, 20, 20, 1);
			addTicker(ans.setVals(n, 30, 40, 60)
					.init(holder.center(), init, normal, -i * 2)
					.setProp(DanmakuItems.Bullet.BUBBLE, abyss ? DyeColor.BLUE : DyeColor.RED));
		}
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
	public static class Intercept extends Ticker<ReimuSpell> {

		@SerialField
		private Vec3 vec = Vec3.ZERO, dir = new Vec3(1, 0, 0);
		@SerialField
		private final double dist = 32;
		@SerialField
		private final double w = 360d / 60 * 3;
		@SerialField
		private final double speed = 2;
		@SerialField
		private final double duration = 80;

		@Override
		public boolean tick(CardHolder holder, ReimuSpell card) {
			var target = holder.target();
			if (target != null) vec = vec.scale(0.95).add(target.scale(0.05));
			var ori = DanmakuHelper.getOrientation(dir);
			for (int i = 0; i < 8; i++) {
				Vec3 off = ori.rotateDegrees(360d / 8 * i);
				Vec3 pos = vec.add(dir.add(off.scale(dist)));
				var nor = DanmakuHelper.getOrientation(off).asNormal();
				for (int j = 0; j < 8; j++) {
					Vec3 vel = nor.rotateDegrees(360d / 8 * j + tick * w).scale(speed)
							.add(off.scale(-1));
					int life = (int) Math.min(duration - tick, 40) + holder.random().nextInt(10);
					var e = holder.prepareDanmaku(life, vel,
							DanmakuItems.Bullet.BUBBLE, DyeColor.YELLOW);
					e.setPos(pos);
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick > duration;
		}
	}

}
