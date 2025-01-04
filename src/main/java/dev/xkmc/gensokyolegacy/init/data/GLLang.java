package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum GLLang {

	INFO$LOADING("Loading..."),
	INFO$BED_UNBOUND("This block is not linked to a structure"),
	INFO$BED_PRESENT("Character is present at (%s, %s, %s)", 3),
	INFO$BED_MISSING("Character is missing for %s", 1),
	INFO$BED_RESPAWN("Character respawning. Remaining time: %s", 1),
	INFO$ENTITY_UNBOUND("This character is not linked to a bed"),
	INFO$ENTITY_BED("Character's bed is at (%s, %s, %s)", 3),
	INFO$ENTITY_REPUTATION("Your reputation: %s", 1),
	INFO$ENTITY_FEED("Feed cool down: %s", 1),
	INFO$STRUCTURE_SCANNING("Scanning Structure...", 0),
	INFO$STRUCTURE_ABNORMAL("Found %s invalid blocks", 1),

	MSG$RESET("Character reset"),
	MSG$REIMU_FLESH("Reimu: You shall not eat it. Last warning.", 0, ChatFormatting.RED),
	MSG$REIMU_WARN("Reimu: Drink some tea and keep your sanity. Last warning.", 0, ChatFormatting.RED),
	MSG$KOISHI_REIMU("Reimu: ???", 0, ChatFormatting.RED),

	TAB$TITLE("Gensokyo Roles", 0),
	TAB$NO_ROLE("Regular Human (No Role)", 0),
	TAB$MAIN_ROLE("%s (%s)", 2),
	TAB$ROLE_PROGRESS("%s - %s", 2),

	COMMAND$SUCCESS("Success"),
	COMMAND$INVALID_ROLE("Error: invalid role id"),

	FLESH$TASTE_HUMAN("Unappealing smell...", 0, ChatFormatting.GRAY),
	FLESH$TASTE_HALF_YOUKAI("Strange flavor...", 0, ChatFormatting.GRAY),
	FLESH$TASTE_YOUKAI("Delicious!", 0, ChatFormatting.GRAY),
	FLESH$FLESH_HUMAN("Weird Meat", 0, null),
	FLESH$FLESH_YOUKAI("Flesh", 0, null),

	ITEM$WAND_BED("Click bed to reset character"),
	ITEM$WAND_BLOCK("Click block to show structure bounds"),
	ITEM$WAND_STRUCTURE("Sneak-click block to show structure option screen"),
	ITEM$WAND_CHARACTER("Click character to reset global character data for you"),
	ITEM$GLASS_PATH("Display character path finding"),
	ITEM$GLASS_CHARACTER("Display character info"),
	ITEM$GLASS_BED("Display bed info"),
	ITEM$HAS_ABILITY("gensokyo roles"),

	ITEM$OBTAIN("Source: ", 0, ChatFormatting.GRAY),
	ITEM$UNKNOWN("???", 0, ChatFormatting.GRAY),
	ITEM$USAGE("Usage: ", 0, ChatFormatting.GRAY),

	ITEM$OBTAIN_FLESH("Kill human mobs with knife while being %s", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_BLOOD("Kill human mobs with knife and have glass bottle in off hand wile being %s", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_FAIRY_ICE("Crafted by Cirno.", 0, ChatFormatting.GRAY),
	ITEM$USAGE_FAIRY_ICE("Throw to deal damage and freeze target.", 0, ChatFormatting.GRAY),
	ITEM$OBTAIN_FROZEN_FROG("Dropped when Cirno freezes a frog.", 0, ChatFormatting.GRAY),
	ITEM$USAGE_FROZEN_FROG("Throw toward target to summon a frog.", 0, ChatFormatting.GRAY),
	ITEM$USAGE_STRAW_HAT("With %s, you can equip it on frogs to allow them to eat raiders", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_SUWAKO_HAT("Drops when frog with hat eats %s different kinds of raiders in front of villagers", 1, ChatFormatting.GRAY),
	ITEM$USAGE_SUWAKO_HAT("Grants constant %s. Allows using Cyan and Lime danmaku without consumption.", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_KOISHI_HAT("Drops when blocking Koishi attacks %s times in a row", 1, ChatFormatting.GRAY),
	ITEM$USAGE_KOISHI_HAT("Grants constant %s. Allows using Blue and Red danmaku without consumption.", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_RUMIA_HAIRBAND("Drops when player defeat Ex. Rumia with Danmaku", 0, ChatFormatting.GRAY),
	ITEM$USAGE_RUMIA_HAIRBAND("Shift player towards %s. Drops heads when killing mobs. Flesh and blood drops no longer require knife (bonus when still using knife).", 1, ChatFormatting.GRAY),
	ITEM$OBTAIN_REIMU_HAIRBAND("Feed Reimu a variety of food", 0, ChatFormatting.GRAY),
	ITEM$USAGE_REIMU_HAIRBAND("Enables creative flight. Your danmaku damage bypasses magical protection.", 0, ChatFormatting.GRAY),
	ITEM$USAGE_CIRNO_HAIRBAND("Shift player towards %s. Your magic damage freezes target (and frogs). Allows using Light Blue danmaku without consumption.", 1, ChatFormatting.GRAY),
	ITEM$USAGE_FAIRY_WINGS("When you are %s, enables creative flight.", 1, ChatFormatting.GRAY),

	;

	private final String def;
	private final int argn;
	private final String key;
	private final @Nullable ChatFormatting format;

	GLLang(String def) {
		this(def, 0);
	}

	GLLang(String def, int argn) {
		this(def, argn, null);
	}

	GLLang(String def, int argn, @Nullable ChatFormatting format) {
		this.def = def;
		this.argn = argn;
		this.key = GensokyoLegacy.MODID + "." + name().toLowerCase(Locale.ROOT).replace("$", ".");
		this.format = format;
	}

	public MutableComponent get(Object... args) {
		if (args.length != argn)
			throw new IllegalArgumentException("for " + name() + ": expect " + argn + " parameters, got " + args.length);
		var ans = Component.translatable(key, args);
		if (format != null) ans.withStyle(format);
		return ans;
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
		RoleCategory.genLang(pvd);
		for (var e : values()) {
			pvd.add(e.key, e.def);
		}

		pvd.add(GensokyoLegacy.MODID + ".subtitle.koishi_ring", "Koishi Phone Call");
	}
}
