package dev.xkmc.gensokyolegacy.content.mechanics.role.effect;

import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRoleHolder;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.l2damagetracker.contents.attack.DamageData;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;

public record RoleDataMap(
		ArrayList<AttributeEntry> attrs,
		ArrayList<ElementalDefence> defence
) implements AttackEffect {

	public void start(PlayerRoleHolder player, int stage) {
		var id = player.role().getRegistryName().withSuffix("_" + stage);
		for (var e : attrs) {
			if (stage < e.start()) continue;
			var ins = player.player().getAttribute(e.attr());
			if (ins == null) continue;
			var mod = new AttributeModifier(id, e.value() * (stage - e.start() + e.base()), e.op());
			ins.addPermanentModifier(mod);
		}
	}

	public void end(PlayerRoleHolder player, int stage) {
		var id = player.role().getRegistryName().withSuffix("_" + stage);
		for (var e : attrs) {
			if (stage < e.start()) continue;
			var ins = player.player().getAttribute(e.attr());
			if (ins == null) continue;
			ins.removeModifier(id);
		}
	}

	public boolean immune(DamageData.Attack cache, int stage) {
		for (var e : defence) {
			if (stage >= e.start()) {
				double rate = e.base() + (stage * e.start()) * e.slope();
				if (rate >= 1 && e.is(cache.getSource())) {
					return true;
				}
			}
		}
		return false;
	}

	public void onDamage(Role role, DamageData.Defence cache, int stage) {
		var id = role.getRegistryName().withSuffix("_" + stage);
		for (var e : defence) {
			if (stage >= e.start()) {
				double rate = e.base() + (stage * e.start()) * e.slope();
				if (e.is(cache.getSource())) {
					cache.addDealtModifier(DamageModifier.multTotal((float) (1 - rate), id));
				}
			}
		}
	}

}
