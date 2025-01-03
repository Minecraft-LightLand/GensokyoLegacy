package dev.xkmc.gensokyolegacy.content.mechanics.role.simple;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.AttributeEffect;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.AttributeEntry;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RumiaRole extends Role {

	public RumiaRole() {
		super(RoleCategory.YOUKAI);
		add(0, new AttributeEffect(
				new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, () -> 0.2f)
		));
		add(1, new AttributeEffect(
				new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, () -> 0.3f)
		));
		add(2, new AttributeEffect(
				new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, () -> 0.5f),
				new AttributeEntry(L2DamageTracker.REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -0.1f)
		));
		add(3, new AttributeEffect(
				new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, () -> 0.6f),
				new AttributeEntry(L2DamageTracker.REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -0.15f)
		));
		add(4, new AttributeEffect(
				new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, () -> 0.7f),
				new AttributeEntry(L2DamageTracker.REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, () -> -0.2f)
		));
	}

}
