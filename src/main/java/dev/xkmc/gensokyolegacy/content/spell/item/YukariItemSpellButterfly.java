package dev.xkmc.gensokyolegacy.content.spell.item;

import dev.xkmc.danmakuapi.content.spell.item.ItemSpell;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class YukariItemSpellButterfly extends ItemSpell {

	@Override
	public void start(Player player, @Nullable LivingEntity target) {
		super.start(player, target);
		//var holder = new PlayerHolder(player, dir, this, target);
		//YukariPartButterfly.launchButterfly(holder, DanmakuItems.Bullet.BUTTERFLY, DyeColor.CYAN, 1);
		//YukariPartButterfly.launchButterfly(holder, DanmakuItems.Bullet.BUTTERFLY, DyeColor.MAGENTA, -1);
	}
}
