package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.gensokyolegacy.content.mechanics.role.RoleCategory;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum GLLang {
	MSG_RESET("Character reset"),
	INFO_LOADING("Loading..."),

	INFO_BED_UNBOUND("This block is not linked to a structure"),
	INFO_BED_PRESENT("Character is present at (%s, %s, %s)", 3),
	INFO_BED_MISSING("Character is missing for %s", 1),
	INFO_BED_RESPAWN("Character respawning. Remaining time: %s", 1),

	INFO_ENTITY_UNBOUND("This character is not linked to a bed"),
	INFO_ENTITY_BED("Character's bed is at (%s, %s, %s)", 3),
	INFO_ENTITY_REPUTATION("Your reputation: %s", 1),
	INFO_ENTITY_FEED("Feed cool down: %s", 1),

	INFO_STRUCTURE_SCANNING("Scanning Structure...", 0),
	INFO_STRUCTURE_ABNORMAL("Found %s invalid blocks", 1),

	ITEM_WAND_BED("Click bed to reset character"),
	ITEM_WAND_BLOCK("Click block to show structure bounds"),
	ITEM_WAND_STRUCTURE("Sneak-click block to show structure option screen"),
	ITEM_WAND_CHARACTER("Click character to reset global character data for you"),
	ITEM_GLASS_PATH("Display character path finding"),
	ITEM_GLASS_CHARACTER("Display character info"),
	ITEM_GLASS_BED("Display bed info"),

	FLESH_TASTE_HUMAN("flesh.taste_human", "Unappealing smell...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_HALF_YOUKAI("flesh.taste_half_youkai", "Strange flavor...", 0, ChatFormatting.GRAY),
	FLESH_TASTE_YOUKAI("flesh.taste_youkai", "Delicious!", 0, ChatFormatting.GRAY),
	FLESH_NAME_HUMAN("flesh.flesh_human", "Weird Meat", 0, null),
	FLESH_NAME_YOUKAI("flesh.flesh_youkai", "Flesh", 0, null),

	OBTAIN("item.obtain", "Source: ", 0, ChatFormatting.GRAY),
	UNKNOWN("item.unknown", "???", 0, ChatFormatting.GRAY),
	USAGE("item.usage", "Usage: ", 0, ChatFormatting.GRAY),
	OBTAIN_FLESH("item.obtain_flesh", "Kill human mobs with knife while being %s", 1, ChatFormatting.GRAY),
	OBTAIN_BLOOD("item.obtain_blood", "Kill human mobs with knife and have glass bottle in off hand wile being %s", 1, ChatFormatting.GRAY),
	ITEM_FAIRY_ICE_OBTAIN("item.obtain_fairy_ice", "Crafted by Cirno.", 0, ChatFormatting.GRAY),
	ITEM_FAIRY_ICE_USAGE("item.usage_fairy_ice", "Throw to deal damage and freeze target.", 0, ChatFormatting.GRAY),
	ITEM_FROZEN_FROG_OBTAIN("item.obtain_frozen_frog", "Dropped when Cirno freezes a frog.", 0, ChatFormatting.GRAY),
	ITEM_FROZEN_FROG_USAGE("item.usage_frozen_frog", "Throw toward target to summon a frog.", 0, ChatFormatting.GRAY),
	USAGE_STRAW_HAT("item.usage_straw_hat", "With %s, you can equip it on frogs to allow them to eat raiders", 1, ChatFormatting.GRAY),
	OBTAIN_SUWAKO_HAT("item.obtain_suwako_hat", "Drops when frog with hat eats %s different kinds of raiders in front of villagers", 1, ChatFormatting.GRAY),
	USAGE_SUWAKO_HAT("item.usage_suwako_hat", "Grants constant %s. Allows using Cyan and Lime danmaku without consumption.", 1, ChatFormatting.GRAY),
	OBTAIN_KOISHI_HAT("item.obtain_koishi_hat", "Drops when blocking Koishi attacks %s times in a row", 1, ChatFormatting.GRAY),
	USAGE_KOISHI_HAT("item.usage_koishi_hat", "Grants constant %s. Allows using Blue and Red danmaku without consumption.", 1, ChatFormatting.GRAY),
	OBTAIN_RUMIA_HAIRBAND("item.obtain_rumia_hairband", "Drops when player defeat Ex. Rumia with Danmaku", 0, ChatFormatting.GRAY),
	USAGE_RUMIA_HAIRBAND("item.usage_rumia_hairband", "Shift player towards %s. Drops heads when killing mobs. Flesh and blood drops no longer require knife (bonus when still using knife).", 1, ChatFormatting.GRAY),
	OBTAIN_REIMU_HAIRBAND("item.obtain_reimu_hairband", "Feed Reimu a variety of food", 0, ChatFormatting.GRAY),
	USAGE_REIMU_HAIRBAND("item.usage_reimu_hairband", "Enables creative flight. Your danmaku damage bypasses magical protection.", 0, ChatFormatting.GRAY),
	USAGE_CIRNO_HAIRBAND("item.usage_cirno_hairband", "Shift player towards %s. Your magic damage freezes target (and frogs). Allows using Light Blue danmaku without consumption.", 1, ChatFormatting.GRAY),
	USAGE_FAIRY_WINGS("item.usage_fairy_wings", "When you are %s, enables creative flight.", 1, ChatFormatting.GRAY),

	REIMU_FLESH("msg.reimu_flesh", "Reimu: You shall not eat it. Last warning.", 0, ChatFormatting.RED),
	REIMU_WARN("msg.reimu_warn", "Reimu: Drink some tea and keep your sanity. Last warning.", 0, ChatFormatting.RED),
	KOISHI_REIMU("msg.koishi_reimu", "Reimu: ???", 0, ChatFormatting.RED),

	;

	private final String def;
	private final int argn;
	private final String key;
	private final @Nullable ChatFormatting format;

	GLLang(String def) {
		this(def, 0);
	}

	GLLang(String def, int argn) {
		this.def = def;
		this.argn = argn;
		this.key = GensokyoLegacy.MODID + ".tooltip." + name().toLowerCase(Locale.ROOT);
		this.format = null;
	}

	GLLang(String key, String def, int argn, @Nullable ChatFormatting format) {
		this.def = def;
		this.argn = argn;
		this.key = GensokyoLegacy.MODID + "." + key;
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
