package dev.xkmc.gensokyolegacy.content.spell.card;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.spellcard.ActualSpellCard;
import dev.xkmc.danmakuapi.content.spell.spellcard.CardHolder;
import dev.xkmc.danmakuapi.init.registrate.DanmakuItems;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.item.DyeColor;

@SerialClass
public class LunaSpell extends ActualSpellCard {

	@Override
	public void tick(CardHolder holder) {
		var target = holder.forward();
		if (tick % 10 == 0 && tick / 10 % 6 < 4) {
			var o = DanmakuHelper.getOrientation(target);
			int offset = holder.random().nextInt(120);
			for (int i = 0; i < 120; i++) {
				if (i / 4 % 2 == 0) continue;
				var dir = o.rotateDegrees((i + offset) * 3);
				var e = holder.prepareDanmaku(40, dir.scale(0.8),
						DanmakuItems.Bullet.BALL, DyeColor.YELLOW);
				holder.shoot(e);
			}
		}
		super.tick(holder);
	}

}
