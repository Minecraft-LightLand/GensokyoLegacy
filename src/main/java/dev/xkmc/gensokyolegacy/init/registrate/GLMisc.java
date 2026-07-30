package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.MenuEntry;
import dev.xkmc.gensokyolegacy.content.ui.FirstDialogMenu;
import dev.xkmc.gensokyolegacy.content.ui.FirstDialogScreen;
import dev.xkmc.gensokyolegacy.content.ui.SimpleDialogMenu;
import dev.xkmc.gensokyolegacy.content.ui.SimpleDialogScreen;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.SR;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GLMisc {

	private static final SR<LootItemConditionType> LIC = SR.of(GensokyoLegacy.REG, Registries.LOOT_CONDITION_TYPE);

	public static final MenuEntry<FirstDialogMenu> DIALOG_FIRST = GensokyoLegacy.REGISTRATE.menu("first_dialog",
			FirstDialogMenu::fromNetwork, () -> FirstDialogScreen::new).register();

	public static final MenuEntry<SimpleDialogMenu> DIALOG_SIMPLE = GensokyoLegacy.REGISTRATE.menu("simple_dialog",
			SimpleDialogMenu::fromNetwork, () -> SimpleDialogScreen::new).register();

	public static void register() {

	}

}
