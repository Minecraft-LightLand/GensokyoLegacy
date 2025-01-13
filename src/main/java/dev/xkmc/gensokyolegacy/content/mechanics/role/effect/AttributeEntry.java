package dev.xkmc.gensokyolegacy.content.mechanics.role.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeEntry(
		Holder<Attribute> attr, AttributeModifier.Operation op, int start, int base, double value
) {
}
