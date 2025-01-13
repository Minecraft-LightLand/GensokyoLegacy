package dev.xkmc.gensokyolegacy.content.effect;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class CharacterEffect extends InherentEffect {

	public final RoleCategory role;

	public CharacterEffect(RoleCategory role, MobEffectCategory category, int color) {
		super(category, color);
		this.role = role;
	}

	@Override
	public boolean applyEffectTick(LivingEntity e, int amplifier) {
		if (e instanceof Player player) {
			int val = 250 * (amplifier + 1);
			role.advanceIfStarted(player, val, val);
		}
		return false;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}
}
