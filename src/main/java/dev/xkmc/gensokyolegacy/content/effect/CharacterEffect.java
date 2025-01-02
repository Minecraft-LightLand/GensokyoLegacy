package dev.xkmc.gensokyolegacy.content.effect;

import dev.xkmc.gensokyolegacy.content.mechanics.role.Role;
import dev.xkmc.l2core.base.effects.api.InherentEffect;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.world.effect.MobEffectCategory;

public class CharacterEffect extends InherentEffect {

	public final Val<? extends Role> role;

	public CharacterEffect(Val<? extends Role> role, MobEffectCategory category, int color) {
		super(category, color);
		this.role = role;
	}

}
