package dev.xkmc.gensokyolegacy.content.spell.item;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.item.ItemSpell;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.content.spell.spellcard.Ticker;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.gensokyolegacy.content.spell.mover.AttachedFreeRotMover;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class MarisaItemSpell extends ItemSpell {

	@Override
	public void start(Player player, @Nullable LivingEntity target) {
		super.start(player, target);
		addTicker(new MasterSpark(player));
	}

	@SerialClass
	public static class MasterSpark extends Ticker<MarisaItemSpell> {

		@SerialField
		private Vec3 target;

		@Nullable
		private Player player;

		public MasterSpark() {

		}

		public MasterSpark(Player player) {
			this.player = player;
		}

		@Override
		public boolean tick(CardHolder holder, MarisaItemSpell card) {
			if (player != null)
				target = RayTraceUtil.getRayTerm(Vec3.ZERO, player.getXRot(), player.getYRot(), 10);
			var cen = holder.center();
			if (tick == 0) {
				var e = holder.prepareLaser(1, cen, target.normalize(), 80,
						DanmakuItems.Laser.LASER, DyeColor.YELLOW);
				e.setupTime(20, 1, 1, 1);
				e.mover = new AttachedFreeRotMover();
				holder.shoot(e);
			}
			if (tick > 20) {
				var forw = target.normalize();
				var rand = holder.random();
				var o = DanmakuHelper.getOrientation(forw);
				for (int i = 0; i < 10; i++) {
					var pos = cen.add(forw.scale(i * 1.4 + 2));
					double x = rand.nextDouble() * 30 - 15;
					double y = rand.nextDouble() * 30 - 15;
					var vec = o.rotateDegrees(x, y);
					var v = rand.nextDouble() + 2;
					var e = holder.prepareDanmaku(40, vec.scale(v),
							DanmakuItems.Bullet.MENTOS, DyeColor.WHITE);
					e.setPos(pos);
					holder.shoot(e);
				}

				for (int i = 0; i < 5; i++) {
					double x = rand.nextDouble() * 90 - 45;
					double y = rand.nextDouble() * 90 - 45;
					var vec = o.rotateDegrees(x, y);
					var v = rand.nextDouble() * 0.3 + 0.6;
					var e = holder.prepareDanmaku(40, vec.scale(v),
							DanmakuItems.Bullet.BALL, DyeColor.YELLOW);
					e.setPos(cen.add(vec.scale(v + 1)));
					holder.shoot(e);
				}
			}
			super.tick(holder, card);
			return tick > 60;
		}

	}


}
