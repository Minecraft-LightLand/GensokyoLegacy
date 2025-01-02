package dev.xkmc.gensokyolegacy.content.mechanics.incense.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.SimpleEntityIncense;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

public class GlowingIncense extends SimpleEntityIncense {

	@Override
	public ParticleOptions getParticleOptions() {
		return ParticleTypes.INSTANT_EFFECT;
	}

	@Override
	public void tickServer(Level level, BlockPos pos, int radius) {
		if (level.getGameTime() % 20 == 0) {
			for (var e : getEntities(level, pos, radius, e -> true)) {
				EffectUtil.addEffect(e, new MobEffectInstance(MobEffects.GLOWING, 100, 0, true, false), null);
			}
		}
	}

}
