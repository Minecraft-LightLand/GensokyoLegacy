package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public enum GLLang {
	MSG_RESET("Character reset"),
	INFO_LOADING("Loading..."),

	INFO_BED_UNBOUND("This bed is not linked to a structure"),
	INFO_BED_PRESENT("Character is present at (%s, %s, %s)", 3),
	INFO_BED_MISSING("Character is missing for %s", 1),
	INFO_BED_RESPAWN("Character respawning. Remaining time: %s", 1),

	INFO_ENTITY_UNBOUND("This character is not linked to a bed"),
	INFO_ENTITY_BED("Character's bed is at (%s, %s, %s)", 3),
	INFO_ENTITY_REPUTATION("Your reputation: %s", 1),
	INFO_ENTITY_FEED("Feed cool down: %s", 1),

	ITEM_WAND_BED("Click bed to reset character"),
	ITEM_WAND_CHARACTER("Click character to reset global character data for you"),
	ITEM_GLASS_PATH("Display character path finding"),
	ITEM_GLASS_CHARACTER("Display character info"),
	ITEM_GLASS_BED("Display bed info");

	private final String def;
	private final int argn;
	private final String key;

	GLLang(String def) {
		this(def, 0);
	}

	GLLang(String def, int argn) {
		this.def = def;
		this.argn = argn;
		this.key = GensokyoLegacy.MODID + ".tooltip." + name().toLowerCase(Locale.ROOT);
	}

	public MutableComponent get(Object... args) {
		if (args.length != argn)
			throw new IllegalArgumentException("for " + name() + ": expect " + argn + " parameters, got " + args.length);
		return Component.translatable(key, args);
	}

	public MutableComponent time(long diff) {
		if (diff < 0) diff = 0;
		int sec = (int) ((diff / 20) % 60);
		int min = (int) ((diff / 1200) % 60);
		int hrs = (int) (diff / 72000);
		var str = hrs == 0 ? "%d:%02d".formatted(min, sec) : "%d:%02d:%02d".formatted(hrs, min, sec);
		return get(str);
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (var e : values()) {
			pvd.add(e.key, e.def);
		}
	}
}