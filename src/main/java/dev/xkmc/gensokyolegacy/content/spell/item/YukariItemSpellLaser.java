package dev.xkmc.gensokyolegacy.content.spell.item;

import dev.xkmc.danmakuapi.content.spell.item.ItemSpell;
import dev.xkmc.gensokyolegacy.content.spell.part.YoukariPartLaser;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class YukariItemSpellLaser extends ItemSpell {

	@Override
	public void start(Player player, @Nullable LivingEntity target) {
		super.start(player, target);
		addTicker(new YoukariPartLaser<>());
	}

}
