package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class YoukaiSourceOverride extends DamageSources {

	public Function<LivingEntity, DamageSource> mobAttack;

	public YoukaiSourceOverride(RegistryAccess registry) {
		super(registry);
	}

	@Override
	public DamageSource mobAttack(LivingEntity mob) {
		if (mobAttack != null) return mobAttack.apply(mob);
		return super.mobAttack(mob);
	}
}
