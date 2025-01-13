package dev.xkmc.gensokyolegacy.content.mechanics.role.loot;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

@SerialClass
public class RoleProgressLootCondition implements LootItemCondition, LootItemCondition.Builder {

	@SerialField
	private final Role role;
	@SerialField
	private final int progress;

	public RoleProgressLootCondition(Role role, int progress) {
		this.role = role;
		this.progress = progress;
	}

	@Override
	public LootItemConditionType getType() {
		return GLMisc.LIC_ROLE.get();
	}

	@Override
	public boolean test(LootContext ctx) {
		var player = ctx.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		if (player == null) return false;
		return RolePlayHandler.progress(player, role) > progress;
	}

	@Override
	public LootItemCondition build() {
		return this;
	}

}
