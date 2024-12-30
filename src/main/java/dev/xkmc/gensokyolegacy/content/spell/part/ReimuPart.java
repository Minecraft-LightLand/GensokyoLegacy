package dev.xkmc.gensokyolegacy.content.spell.part;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.mover.RectMover;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class ReimuPart<T> extends Ticker<T> {

	@SerialField
	private Vec3 pos, init, normal, target1;
	@SerialField
	private int r0, r1, n;
	@SerialField
	private int t0, t1, t2, dt;
	@SerialField
	private double termSpeed, tilt;
	@SerialField
	private DanmakuItems.Bullet bullet;
	@SerialField
	private DyeColor color;

	public ReimuPart() {
		setRad(8, 6, 20, 20, 1);
		setVals(20, 30, 40, 60);
		setProp(DanmakuItems.Bullet.CIRCLE, DyeColor.RED);
	}

	public ReimuPart<T> setRad(int r0, int r1, int t0, int t1, double term) {
		this.r0 = r0;
		this.r1 = r1;
		this.t0 = t0;
		this.t1 = t1;
		this.termSpeed = term;
		return this;
	}

	public ReimuPart<T> setVals(int n, double tilt, int lifeLo, int lifeHi) {
		this.n = n;
		this.tilt = tilt;
		this.t2 = lifeLo;
		this.dt = lifeHi - lifeLo;
		return this;
	}

	public ReimuPart<T> setProp(DanmakuItems.Bullet bullet, DyeColor color) {
		this.bullet = bullet;
		this.color = color;
		return this;
	}

	public ReimuPart<T> init(Vec3 pos, Vec3 init, Vec3 normal, int tick) {
		this.pos = pos;
		this.init = init;
		this.normal = normal;
		this.tick = tick;
		return this;
	}

	@Override
	public boolean tick(CardHolder holder, T card) {
		step(holder);
		super.tick(holder, card);
		return false;
	}

	private void step(CardHolder holder) {
		var le = holder.target();
		if (le == null) return;
		var r = holder.random();
		if (init == null) {
			pos = holder.center();
			var dir = le.subtract(holder.center()).normalize();
			init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, tilt * (r.nextDouble() * 2 - 1));
			normal = dir.cross(init);
		}
		if (tick < 0) return;
		if (tick == 0) {
			DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
			double acc = r0 * 2d / t0 / t0;
			for (int i = 0; i < n; i++) {
				var front = o0.rotateDegrees(360.0 / n * i);
				var vec = front.scale(acc * t0);
				var e = holder.prepareDanmaku(t0, vec, bullet, DyeColor.LIGHT_GRAY);
				e.mover = new RectMover(pos, vec, front.scale(-acc));
				holder.shoot(e);
			}
		}
		if (tick == t0) {
			target1 = le;
			DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
			double acc = r1 * 2d / t1 / t1;
			for (int i = 0; i < n; i++) {
				var f0 = o0.rotateDegrees(360.0 / n * i);
				var p0 = pos.add(f0.scale(r0));
				var f1 = target1.subtract(p0).normalize();
				var vec = f1.scale(acc * t1);
				var e = holder.prepareDanmaku(t1, vec, bullet, DyeColor.PURPLE);
				e.setPos(p0);
				e.mover = new RectMover(p0, vec, f1.scale(-acc));
				holder.shoot(e);
			}
		}
		if (tick == t0 + t1) {
			DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(init, normal);
			for (int i = 0; i < n; i++) {
				var f0 = o0.rotateDegrees(360.0 / n * i);
				var p0 = pos.add(f0.scale(r0));
				var f1 = target1.subtract(p0).normalize();
				var p1 = p0.add(f1.scale(r1));
				var f2 = le.subtract(p1).normalize();
				var vec = f2.scale(termSpeed);
				int t = t2 + r.nextInt(dt);
				var e = holder.prepareDanmaku(t, vec, bullet, color);
				e.setPos(p1);
				e.mover = new RectMover(p1, vec, Vec3.ZERO);
				holder.shoot(e);
			}
		}
	}

}
