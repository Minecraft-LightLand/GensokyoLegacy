package dev.xkmc.gensokyolegacy.content.effect;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.youkaishomecoming.content.effect.EmptyEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class NativeGodBlessEffect extends EmptyEffect {

	public NativeGodBlessEffect(MobEffectCategory category, int color) {
		super(category, color);
		var uuid = GensokyoLegacy.loc("native_god_bless");
		addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, uuid, 1f,
				AttributeModifier.Operation.ADD_VALUE);
		addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, uuid, 1f,
				AttributeModifier.Operation.ADD_VALUE);
		addAttributeModifier(Attributes.MOVEMENT_SPEED, uuid, 0.2f,
				AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
	}

}
