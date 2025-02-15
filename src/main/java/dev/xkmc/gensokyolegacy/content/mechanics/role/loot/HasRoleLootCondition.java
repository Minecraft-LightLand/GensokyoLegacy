package dev.xkmc.gensokyolegacy.content.mechanics.role.loot;

import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record HasRoleLootCondition(int progress) implements LootItemCondition {

	@Override
	public LootItemConditionType getType() {
		return GLMisc.LIC_ANY_ROLE.get();
	}

	@Override
	public boolean test(LootContext ctx) {
		var player = ctx.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		if (player == null) return false;
		var role = GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, null);
		return role != null && role.data().getProgress() >= progress;
	}

}
