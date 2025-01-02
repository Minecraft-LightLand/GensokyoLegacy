package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.Role;
import net.minecraft.world.entity.player.Player;

public record PlayerRoleHolder(Player player, Role role, PlayerRoleData data) {
}
