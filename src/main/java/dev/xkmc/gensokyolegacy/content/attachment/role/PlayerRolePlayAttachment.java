package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleStage;
import dev.xkmc.l2core.capability.player.PlayerCapabilityTemplate;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SerialClass
public class PlayerRolePlayAttachment extends PlayerCapabilityTemplate<PlayerRolePlayAttachment> {

	@SerialField
	public final Map<Role, PlayerRoleData> map = new LinkedHashMap<>();

	@SerialField
	private RoleStage previous = null;

	@Override
	public void onClone(Player player, boolean isWasDeath) {
		if (isWasDeath && !player.level().getGameRules().getRule(GameRules.RULE_KEEPINVENTORY).get()) {
			map.clear();
		}
		tickActual(player, null);
	}

	@Override
	public void tick(Player player) {
		var holder = getMaxAbility(player, null);
		tickActual(player, holder);
	}

	private void tickActual(Player player, @Nullable PlayerRoleHolder holder) {
		if (holder != null) {
			var next = new RoleStage(holder.role(), holder.data().getProgress() / 500);
			if (previous == null || !previous.equals(next)) {
				if (previous != null) {
					var role = previous.role();
					role.end(new PlayerRoleHolder(player, this, role,
							map.getOrDefault(role, new PlayerRoleData())), previous.stage());
				}
				next.role().start(holder, next.stage());
				previous = next;
			}
			holder.role().tick(holder, next.stage());
		} else if (previous != null) {
			var role = previous.role();
			role.end(new PlayerRoleHolder(player, this, role,
					map.getOrDefault(role, new PlayerRoleData())), previous.stage());
			previous = null;
		}
	}

	@Nullable
	public PlayerRoleHolder getData(Player player, Role role) {
		var data = map.get(role);
		if (data == null || data.getProgress() == 0) return null;
		return new PlayerRoleHolder(player, this, role, data);
	}

	public PlayerRoleHolder getOrCreate(Player player, Role role) {
		return new PlayerRoleHolder(player, this, role, map.computeIfAbsent(role, r -> new PlayerRoleData()));
	}

	@Nullable
	public PlayerRoleHolder getMaxAbility(Player player, @Nullable RoleCategory category) {
		Map.Entry<Role, PlayerRoleData> max = null;
		for (var e : map.entrySet()) {
			if (e.getValue().getProgress() == 0)
				continue;
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
			if (ent.getValue().getProgress() == 0) continue;
			list.add(new PlayerRoleHolder(player, this, ent.getKey(), ent.getValue()));
		}
		return list;
	}

	protected void cleanUp() {
		map.values().removeIf(e -> e.getProgress() == 0);
	}

	public void commandSetRolePoints(Role role, int point) {
		map.clear();
		var data = new PlayerRoleData();
		data.advance(point, point);
		map.put(role, data);
	}

	public void commandClearRoles() {
		map.clear();
	}

}