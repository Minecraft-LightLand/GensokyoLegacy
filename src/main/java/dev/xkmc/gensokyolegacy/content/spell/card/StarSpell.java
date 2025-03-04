package dev.xkmc.gensokyolegacy.content.spell.card;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class StarSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		var target = holder.forward();
		if (tick % 10 == 0 && tick / 10 % 6 < 4) {
			var o = DanmakuHelper.getOrientation(target);
			double x = holder.random().nextGaussian() * 20;
			double y = holder.random().nextGaussian() * 5;
			var vec = o.rotateDegrees(x, y);
			addTicker(new ShootingStar(holder.center(), vec));
		}
		super.tick(holder);
	}

	@SerialClass
	public static class ShootingStar extends Ticker<StarSpell> {

		@SerialField
		private Vec3 pos, dir;

		public ShootingStar() {

		}

		public ShootingStar(Vec3 pos, Vec3 vec) {
			this.pos = pos;
			this.dir = vec;
		}

		@Override
		public boolean tick(CardHolder holder, StarSpell card) {
			if (dir == null) return true;
			if (tick == 0) {
				var e = holder.prepareDanmaku(60, dir.scale(0.8),
						DanmakuItems.Bullet.MENTOS, DyeColor.RED);
				holder.shoot(e);
			}
			if (tick >= 2) {
				var o = DanmakuHelper.getOrientation(dir).asNormal();
				var p = pos.add(dir.scale(0.8 * tick - 0.4));
				for (int i = 0; i < 2; i++) {
					double x = holder.random().nextDouble() * 360;
					double y = holder.random().nextGaussian() * 10;
					var vec = o.rotateDegrees(x, y).scale(0.4);
					var e = holder.prepareDanmaku(80, vec,
							DanmakuItems.Bullet.SPARK, DyeColor.BLUE);
					e.setPos(p);
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick > 60;
		}
	}

}
