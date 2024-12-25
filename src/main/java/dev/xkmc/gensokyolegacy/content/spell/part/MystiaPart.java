
package dev.xkmc.gensokyolegacy.content.spell.part;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.mover.CompositeMover;
import dev.xkmc.danmakuapi.content.spell.mover.RectMover;
import dev.xkmc.danmakuapi.content.spell.mover.ZeroMover;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class MystiaPart<T> extends Ticker<T> {

	@SerialField
	private Vec3 dir;

	@SerialField
	private int rot;

	public MystiaPart() {

	}

	public MystiaPart(int rot) {
		this.rot = rot;
	}

	@Override
	public boolean tick(CardHolder holder, T card) {
		if (dir == null) dir = holder.forward().normalize();
		int n = 15, dt = 2, dy = 3;
		if (tick % dt == 0) {
			var o = DanmakuHelper.getOrientation(dir);
			for (int i = 0; i < dy; i++) {
				double index = rot * (1d * tick / dt - (n - 0.5) / 2);
				var vec = o.rotateDegrees(index * 10, (i - (dy - 0.5) / 2) * 10);
				for (int j = 0; j < 10; j++) {
					int t0 = 15 - j / 2;
					int t1 = 30 - j - t0;
					double speed = 1.6;
					var e = holder.prepareDanmaku(80, vec, DanmakuItems.Bullet.MENTOS, DyeColor.LIME);
					var mover = new CompositeMover();
					mover.add(j + 1, new ZeroMover(vec, vec, j + 1));
					var p0 = holder.center();
					var v = vec.scale(speed);
					var a = vec.scale(-speed / t0);
					mover.add(t0, new RectMover(p0, v, a));
					var p1 = p0.add(v.scale(t0 * 0.5));
					mover.add(t1, new ZeroMover(vec, vec, t1));
					mover.add(20, new RectMover(p1, Vec3.ZERO, a.scale(-1)));
					e.mover = mover;
					holder.shoot(e);
				}
			}
		}
		super.tick(holder, card);
		return tick >= n * dt;
	}

}