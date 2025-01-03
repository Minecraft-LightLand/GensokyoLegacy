package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.function.DoubleSupplier;

public record AttributeEntry(
		Holder<Attribute> attr, AttributeModifier.Operation op, DoubleSupplier value
) {
}
