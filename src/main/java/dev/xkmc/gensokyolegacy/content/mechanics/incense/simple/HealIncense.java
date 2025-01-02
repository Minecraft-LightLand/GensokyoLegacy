package dev.xkmc.gensokyolegacy.content.mechanics.incense.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.SimpleEntityIncense;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class HealIncense extends SimpleEntityIncense {

	@Override
	public void tickServer(Level level, BlockPos pos, int radius) {
		float factor = 0.05f;//TODO config
		if (level.getGameTime() % 20 == 0) {
			for (var e : getEntities(level, pos, radius, e -> !(e instanceof Enemy))) {
				e.heal(e.getMaxHealth() * factor);
			}
		}
	}

}
