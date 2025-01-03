package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.RoleCategory;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SerialClass
public class PlayerRolePlayAttachment extends PlayerCapabilityTemplate<PlayerRolePlayAttachment> {

	public final Map<Role, PlayerRoleData> map = new LinkedHashMap<>();

	@Nullable
	public PlayerRoleHolder getData(Player player, Role role) {
		return new PlayerRoleHolder(player, this, role, map.get(role));
	}

	public PlayerRoleHolder getOrCreate(Player player, Role role) {
		return new PlayerRoleHolder(player, this, role, map.computeIfAbsent(role, r -> new PlayerRoleData()));
	}

	@Nullable
	public PlayerRoleHolder getMaxAbility(Player player, @Nullable RoleCategory category) {
		Map.Entry<Role, PlayerRoleData> max = null;
		for (var e : map.entrySet()) {
			if (category != null && e.getKey().getCategory() != category)
				continue;
			if (max == null || e.getValue().getProgress() > max.getValue().getProgress())
				max = e;
		}
		return max == null ? null : new PlayerRoleHolder(player, this, max.getKey(), max.getValue());
	}

	public List<PlayerRoleHolder> getAll(Player player) {
		List<PlayerRoleHolder> list = new ArrayList<>();
		for (var ent : map.entrySet()) {
			list.add(new PlayerRoleHolder(player, this, ent.getKey(), ent.getValue()));
		}
		return list;
	}

	protected void cleanUp() {
		map.values().removeIf(e -> e.getProgress() == 0);
	}

}