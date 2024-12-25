package dev.xkmc.gensokyolegacy.content.spell.part;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class YoukariPartLaser<T> extends Ticker<T> {

	@SerialField
	private Vec3 pos, forward;

	@Override
	public boolean tick(CardHolder holder, T card) {
		if (tick == 0) {
			forward = holder.forward();
			forward = forward.multiply(1, 0.5, 1).normalize();
			pos = holder.center();
		}
		var ori = DanmakuHelper.getOrientation(forward);
		var r = holder.random();
		addLaserBeams(holder, pos, ori.rotateDegrees(-45), 1 + tick * 0.5, r.nextDouble(), DyeColor.RED);
		addLaserBeams(holder, pos, ori.rotateDegrees(45), 1 + tick * 0.5, r.nextDouble(), DyeColor.BLUE);
		if (tick == 20) {
			shootGroup(holder, DyeColor.RED);
		}
		if (tick == 40) {
			shootGroup(holder, DyeColor.BLUE);
		}
		super.tick(holder, card);
		return tick > 120;
	}

	private void shootGroup(CardHolder holder, DyeColor color) {
		double speed = 1;
		double dv = 0.5;
		double dev = 30;
		int n0 = 5;
		int n1 = 50;
		int life = 60;
		int dl = 20;

		var forward = holder.forward();
		var rand = holder.random();
		var ori = DanmakuHelper.getOrientation(forward);

		for (int i = 0; i < n0; i++) {
			double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
			double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n0;
			double sp = speed - dv / n0 * i;
			var vec = ori.rotateDegrees(d0, d1).scale(sp);
			int lf = life + rand.nextInt(dl);
			holder.shoot(holder.prepareDanmaku(lf, vec, DanmakuItems.Bullet.BUBBLE, color));
		}
		for (int i = 0; i < n1; i++) {
			double d0 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
			double d1 = (rand.nextDouble() * 2 - 1) * dev * i / n1;
			double sp = speed - dv / n1 * i;
			var vec = ori.rotateDegrees(d0, d1).scale(sp);
			int lf = life + rand.nextInt(dl);
			holder.shoot(holder.prepareDanmaku(lf, vec, DanmakuItems.Bullet.MENTOS, color));
		}
	}

	private void addLaserBeams(CardHolder holder, Vec3 pos, Vec3 dir, double step, double r, DyeColor color) {
		Vec3 p = pos.add(dir.scale(step));
		var ori = DanmakuHelper.getOrientation(dir).rotate(Math.PI / 2, r * Math.PI * 2);
		holder.shoot(holder.prepareLaser(100, p, ori, 80, DanmakuItems.Laser.LASER, color));
	}

}
