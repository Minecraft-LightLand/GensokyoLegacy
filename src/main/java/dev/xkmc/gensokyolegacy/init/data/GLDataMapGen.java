package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.AttributeEntry;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleAttributeData;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLMechanics;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.ArrayList;
import java.util.List;

public class GLDataMapGen {

	public static void dataMapGen(RegistrateDataMapProvider pvd) {
		GLStructureGen.dataMap(pvd);
		{
			var builder = pvd.builder(GLMechanics.ROLE_ATTRIBUTE.reg());
			builder.add(GLMechanics.RUMIA.id(), new RoleAttributeData(new ArrayList<>(List.of(
					new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0, 1, 0.1),
					new AttributeEntry(L2DamageTracker.REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, 2, 2, -0.05)
			))), false);
			builder.add(GLMechanics.VAMPIRE.id(), new RoleAttributeData(new ArrayList<>(List.of(
					new AttributeEntry(Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, 0, 1, 4),
					new AttributeEntry(L2DamageTracker.REGEN, AttributeModifier.Operation.ADD_VALUE, 0, 0, 0.2),
					new AttributeEntry(L2DamageTracker.REDUCTION, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, 2, 2, -0.05)
			))), false);
			builder.add(GLMechanics.ICE_FAIRY.id(), new RoleAttributeData(new ArrayList<>(List.of(
					new AttributeEntry(Attributes.MAX_HEALTH, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, 0, 1, -0.1),
					new AttributeEntry(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, 0, 1, -0.1),
					new AttributeEntry(L2DamageTracker.REGEN, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, 2, 1, 0.5)
			))), false);
		}
	}

}
