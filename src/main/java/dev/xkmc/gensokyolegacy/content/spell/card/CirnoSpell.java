package dev.xkmc.gensokyolegacy.content.spell.card;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.danmakuapi.content.spell.mover.RectMover;
import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class CirnoSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		super.tick(holder);
		if (tick % 10 == 0) {
			var le = holder.target();
			if (le == null) return;
			var dir = holder.forward();
			if (dir.multiply(1, 0, 1).length() > 0.1) {
				dir = dir.multiply(1, 0, 1).normalize();
			}
			var ori = DanmakuHelper.getOrientation(dir);
			var ans = new StateChange();
			ans.pos = holder.center();
			ans.init = ori.rotateDegrees(180);
			ans.normal = ori.normal();
			addTicker(ans);
		}
	}

	@SerialClass
	public static class StateChange extends Ticker<CirnoSpell> {

		@SerialField
		private Vec3 pos, init, normal;
		@SerialField
		private int r0 = 12, n = 3, m = 4;
		@SerialField
		private int t0 = 20, t2 = 40, dt = 20;
		@SerialField
		private double termSpeed = 1, dr = 20;

		@Override
		public boolean tick(CardHolder holder, CirnoSpell card) {
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
				init = DanmakuHelper.getOrientation(dir).rotateDegrees(90, 0);
				normal = dir.cross(init);
			}
			if (tick < 0) return;
			if (tick == 0) {
				var o0 = DanmakuHelper.getOrientation(init, normal);
				double acc = r0 * 2d / t0 / t0;
				for (int i = 0; i < n; i++) {
					var front = o0.rotateDegrees(360.0 / n * i);
					var vec = front.scale(acc * t0);
					var e = holder.prepareDanmaku(t0, vec, DanmakuItems.Bullet.MENTOS, DyeColor.LIGHT_BLUE);
					e.mover = new RectMover(pos, vec, front.scale(-acc));
					holder.shoot(e);
				}
			}
			if (tick == t0) {
				var o0 = DanmakuHelper.getOrientation(init, normal);
				for (int i = 0; i < n; i++) {
					var f0 = o0.rotateDegrees(360.0 / n * i);
					var p0 = pos.add(f0.scale(r0));
					var f1 = le.subtract(p0).normalize();
					var o1 = DanmakuHelper.getOrientation(f1);
					for (int j = 0; j < m; j++) {
						var vec = o1.rotateDegrees((j - (m - 1) * 0.5) * dr).scale(termSpeed);
						int t = t2 + r.nextInt(dt);
						var e = holder.prepareDanmaku(t, vec, DanmakuItems.Bullet.BALL, DyeColor.LIGHT_BLUE);
						e.setPos(p0);
						e.mover = new RectMover(p0, vec, Vec3.ZERO);
						holder.shoot(e);
					}
				}
			}
		}

	}


}
