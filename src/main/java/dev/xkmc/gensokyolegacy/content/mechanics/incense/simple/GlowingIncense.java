package dev.xkmc.gensokyolegacy.content.mechanics.incense.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.Incense;
import dev.xkmc.l2core.base.effects.EffectUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

public class GlowingIncense extends Incense {

	@Override
	public void tick(Level level, BlockPos pos, int radius) {
		if (level.isClientSide()) {
			addParticles(level, pos, radius, 20, ParticleTypes.INSTANT_EFFECT);
			return;
		}
		if (level.getGameTime() % 20 == 0) {
			for (var e : getEntities(level, pos, radius, e -> true)) {
				EffectUtil.addEffect(e, new MobEffectInstance(MobEffects.GLOWING, 100, 0, true, false), null);
			}
		}
	}

}
