package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.gensokyolegacy.content.ui.dialog.FirstDialogMenu;
import dev.xkmc.gensokyolegacy.content.ui.dialog.FirstDialogScreen;
import dev.xkmc.gensokyolegacy.content.ui.dialog.SimpleDialogMenu;
import dev.xkmc.gensokyolegacy.content.ui.dialog.SimpleDialogScreen;
import dev.xkmc.gensokyolegacy.content.ui.quest.QuestTab;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GLMisc {

	private static final SR<LootItemConditionType> LIC = SR.of(GensokyoLegacy.REG, Registries.LOOT_CONDITION_TYPE);

	public static final MenuEntry<FirstDialogMenu> DIALOG_FIRST = GensokyoLegacy.REGISTRATE.menu("first_dialog",
			FirstDialogMenu::fromNetwork, () -> FirstDialogScreen::new).register();

	public static final MenuEntry<SimpleDialogMenu> DIALOG_SIMPLE = GensokyoLegacy.REGISTRATE.menu("simple_dialog",
			SimpleDialogMenu::fromNetwork, () -> SimpleDialogScreen::new).register();


	public static final ResourceLocation DUMMY = L2Tabs.loc(GensokyoLegacy.MODID);
	public static final SR<TabToken<?, ?>> TAB_REG = SR.of(GensokyoLegacy.REG, L2Tabs.TABS.reg());

	public static final Val<TabToken<InvTabData, QuestTab>> QUEST_TAB =
			TAB_REG.reg("golem", () -> L2Tabs.GROUP.registerTab(
					() -> QuestTab::new, GLLang.QUEST$TAB.get()));

	public static void register() {

	}

}
