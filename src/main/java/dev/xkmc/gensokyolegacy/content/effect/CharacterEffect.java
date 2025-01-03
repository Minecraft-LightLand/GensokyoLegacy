package dev.xkmc.gensokyolegacy.content.effect;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class CharacterEffect extends InherentEffect {

	public final RoleCategory role;

	public CharacterEffect(RoleCategory role, MobEffectCategory category, int color) {
		super(category, color);
		this.role = role;
	}

}
