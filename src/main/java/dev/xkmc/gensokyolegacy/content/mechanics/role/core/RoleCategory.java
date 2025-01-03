package dev.xkmc.gensokyolegacy.content.mechanics.role.core;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public enum RoleCategory {
	HUMAN("Human"),
	YOUKAI("Youkai"),
	FAIRY("Fairy"),
	;

	private final String key, def;

	RoleCategory(String def) {
		this.key = GensokyoLegacy.MODID + ".role_category." + name().toLowerCase(Locale.ROOT);
		this.def = def;
	}


	public void advanceIfStarted(Player player, int max, int val) {
		var data = GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, this);
		if (data != null) data.advance(max, val);
	}

	public Component getName() {
		return Component.translatable(key);
	}

	public static void genLang(RegistrateLangProvider pvd) {
		for (var e : RoleCategory.values()) {
			pvd.add(e.key, e.def);
		}
	}

}
