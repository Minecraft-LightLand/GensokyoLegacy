package dev.xkmc.gensokyolegacy.content.mechanics.incense.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.SimpleEntityIncense;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class CleanseIncense extends SimpleEntityIncense {

	@Override
	public void tickServer(Level level, BlockPos pos, int radius) {
		if (level.getGameTime() % 20 == 0) {
			for (var e : getEntities(level, pos, radius, e -> !e.getActiveEffectsMap().isEmpty())) {
				e.removeEffectsCuredBy(EffectCures.MILK);
			}
		}
	}

}
