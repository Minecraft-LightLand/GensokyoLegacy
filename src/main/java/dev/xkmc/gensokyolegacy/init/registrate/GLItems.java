package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.gensokyolegacy.content.item.DebugGlasses;
import dev.xkmc.gensokyolegacy.content.item.DebugWand;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;

public class GLItems {

	public static final ItemEntry<DebugGlasses> DEBUG_GLASSES;
	public static final ItemEntry<DebugWand> DEBUG_WAND;

	static {
		DEBUG_GLASSES = GensokyoLegacy.REGISTRATE.item("debug_glasses", p -> new DebugGlasses(p.stacksTo(1)))
				.defaultModel().defaultLang().register();

		DEBUG_WAND = GensokyoLegacy.REGISTRATE.item("debug_wand", p -> new DebugWand(p.stacksTo(1)))
				.defaultModel().defaultLang().register();
	}

	public static void register() {

	}

}
