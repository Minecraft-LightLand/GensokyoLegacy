package dev.xkmc.gensokyolegacy.content.mechanics.incense.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.Incense;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.EffectCures;

public class CleanseIncense extends Incense {

	@Override
	public void tick(Level level, BlockPos pos, int radius) {
		if (level.isClientSide()) {
			addParticles(level, pos, radius, 20, ParticleTypes.COMPOSTER);
			return;
		}
		if (level.getGameTime() % 20 == 0) {
			for (var e : getEntities(level, pos, radius, e -> !e.getActiveEffectsMap().isEmpty())) {
				e.removeEffectsCuredBy(EffectCures.MILK);
			}
		}
	}

}
