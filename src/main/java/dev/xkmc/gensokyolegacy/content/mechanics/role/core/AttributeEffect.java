package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRoleHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttributeEffect(
		AttributeEntry... attrs
) implements RoleEffect {

	@Override
	public void start(PlayerRoleHolder player, int stage) {
		var id = player.role().getRegistryName().withSuffix("_" + stage);
		for (var e : attrs) {
			var ins = player.player().getAttribute(e.attr());
			if (ins == null) continue;
			var mod = new AttributeModifier(id, e.value().getAsDouble(), e.op());
			ins.addPermanentModifier(mod);
		}
	}

	@Override
	public void end(PlayerRoleHolder player, int stage) {
		var id = player.role().getRegistryName().withSuffix("_" + stage);
		for (var e : attrs) {
			var ins = player.player().getAttribute(e.attr());
			if (ins == null) continue;
			ins.removeModifier(id);
		}
	}

}
