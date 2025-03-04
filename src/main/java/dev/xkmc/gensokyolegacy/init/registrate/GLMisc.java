package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.client.tab.TabRole;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.HasRoleLootCondition;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleCategoryLootCondition;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleProgressLootCondition;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.serialization.codec.MapCodecAdaptor;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GLMisc {

	public static final Val<TabToken<InvTabData, TabRole>> TAB_ROLE = SR.of(GensokyoLegacy.REG, L2Tabs.TABS.reg())
			.reg("role", () -> L2Tabs.GROUP.registerTab(() -> TabRole::new, GLLang.TAB$TITLE.get()));

	private static final SR<LootItemConditionType> LIC = SR.of(GensokyoLegacy.REG, Registries.LOOT_CONDITION_TYPE);
	public static final Val<LootItemConditionType> LIC_ROLE = LIC.reg("role",
			() -> new LootItemConditionType(MapCodecAdaptor.of(RoleProgressLootCondition.class)));
	public static final Val<LootItemConditionType> LIC_CATEGORY = LIC.reg("role_category",
			() -> new LootItemConditionType(MapCodecAdaptor.of(RoleCategoryLootCondition.class)));
	public static final Val<LootItemConditionType> LIC_ANY_ROLE = LIC.reg("any_role",
			() -> new LootItemConditionType(MapCodecAdaptor.of(HasRoleLootCondition.class)));

	public static void register() {

	}

}
