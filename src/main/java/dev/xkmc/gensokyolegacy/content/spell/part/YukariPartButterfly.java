package dev.xkmc.gensokyolegacy.content.spell.part;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.mover.CompositeMover;
import dev.xkmc.danmakuapi.content.spell.mover.PolarMover;
import dev.xkmc.danmakuapi.content.spell.mover.RectMover;
import dev.xkmc.danmakuapi.content.spell.mover.ZeroMover;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

public class YukariPartButterfly {

	public static void launchButterfly(CardHolder holder, DanmakuItems.Bullet type, DyeColor color, int dire) {
		var r = holder.random();
		var pos = holder.center();
		DanmakuHelper.Orientation o0 = DanmakuHelper.getOrientation(holder.forward());

		int n = 100;
		int mrange = 12, vrange = 8;
		int t0 = 40;
		int t1 = 10;
		double tvr = 0.8;
		int t2 = 10;
		int t3 = 30;
		int t4 = 40;
		double avar = Math.PI / 4;

		float wvr = (float) (tvr / mrange) * dire;
		int total = t0 + t1 + t2 + t3 + t4;
		for (int i = 0; i < n; i++) {
			double a0 = 2 * Math.PI / n * i;
			double ver = (r.nextDouble() * 2 - 1) * avar;
			Vec3 a1 = o0.rotate(a0, ver);
			Vec3 vn = o0.rotate(a0, ver + Math.PI / 2);
			float range = mrange + vrange * (float) (r.nextDouble() * 2 - 1);
			float va = range * 2 / (t0 * t0);
			float vr = va * t0;
			var mover = new CompositeMover();
			var a2 = PolarMover.ofPlane(pos, a1, vn)
					.radial(range, 0, 0).angular(0, wvr, 0).dir(0)
					.scale(100).normalize();
			var polar0 = PolarMover.ofPlane(pos, a1, vn)
					.radial(range, 0, 0).angular(0, 0, wvr / t2);
			var polar1 = polar0.copy().atTime(t2).clearAccel();
			var rect = polar1.copy().atTime(t3).toRect();
			var v1 = a1.scale(vr);
			mover.add(t0, new RectMover(pos, v1, a1.scale(-va)));
			mover.add(t1, new ZeroMover(a1, a2, t1));
			mover.add(t2, polar0);
			mover.add(t3, polar1);
			mover.add(t4, rect);
			var danmaku = holder.prepareDanmaku(total + r.nextInt(40), v1, type, color);
			danmaku.mover = mover;
			holder.shoot(danmaku);
		}
	}


}
