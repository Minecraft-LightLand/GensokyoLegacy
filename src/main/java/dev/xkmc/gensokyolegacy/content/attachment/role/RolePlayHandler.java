package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.core.Role;
import dev.xkmc.gensokyolegacy.content.mechanics.role.core.RoleCategory;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RolePlayHandler {

	@Nullable
	public static Player getPlayer() {
		if (FMLEnvironment.dist == Dist.CLIENT)
			return Proxy.getPlayer();
		return null;
	}

	public static boolean showInfo(Player player) {
		return player.getAbilities().instabuild || hasAbility(player);
	}

	public static boolean showInfo() {
		var player = getPlayer();
		return player != null && showInfo(player);
	}

	public static double progress(Role role) {
		var player = getPlayer();
		if (player == null) return 0;
		return progress(player, role);
	}

	public static double progress(RoleCategory role) {
		var player = getPlayer();
		if (player == null) return 0;
		return progress(player, role);
	}

	public static int progress(Player player, Role role) {
		var data = GLMeta.ABILITY.type().getOrCreate(player).getData(player, role);
		return data == null ? 0 : data.data().getProgress();
	}

	public static int progress(Player player, RoleCategory role) {
		var data = GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, role);
		return data == null ? 0 : data.data().getProgress();
	}

	public static boolean transitioning(Player player) {
		var max = GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, null);
		return max != null && max.data().getProgress() < 1000;
	}

	public static boolean hasAbility(Player player) {
		return player.getAbilities().instabuild || GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, null) != null;
	}

	public static Component tooltipStart() {
		return GLLang.ITEM$HAS_ABILITY.get();
	}

	public static boolean is(Player player, RoleCategory category) {
		return GLMeta.ABILITY.type().getOrCreate(player).getMaxAbility(player, category) != null;
	}

	public static void addTooltips(List<Component> list, @Nullable Component obtain, @Nullable Component usage) {
		if (showInfo()) {
			if (obtain != null)
				list.add(GLLang.ITEM$OBTAIN.get().append(obtain));
			if (usage != null)
				list.add(GLLang.ITEM$USAGE.get().append(usage));
		} else {
			if (obtain != null)
				list.add(GLLang.ITEM$OBTAIN.get().append(GLLang.ITEM$UNKNOWN.get()));
			if (usage != null)
				list.add(GLLang.ITEM$USAGE.get().append(GLLang.ITEM$UNKNOWN.get()));
		}
	}

}
