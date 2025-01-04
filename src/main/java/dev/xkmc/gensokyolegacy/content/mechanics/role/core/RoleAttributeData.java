package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRoleHolder;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;

public record RoleAttributeData(
		ArrayList<AttributeEntry> attrs
) {

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

}
