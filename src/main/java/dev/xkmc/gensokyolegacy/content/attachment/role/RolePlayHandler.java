package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.gensokyolegacy.content.mechanics.role.Role;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2core.util.Proxy;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
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
		return player.getAbilities().instabuild || startTransition(player);
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

	public static double progress(Player player, Role role) {
		return 0;//TODO
	}

	public static boolean transitioning(Player player) {
		return false; //TODO
	}

	public static boolean startTransition(Player player) {
		return player.getAbilities().instabuild;//TODO
	}

	public static Component tooltipStart() {
		return Component.literal("");//TODO
	}

	public static boolean isYoukai(LivingEntity le) {
		return false;//TODO
	}

	public static void addTooltips(List<Component> list, @Nullable Component obtain, @Nullable Component usage) {
		if (showInfo()) {
			if (obtain != null)
				list.add(GLLang.OBTAIN.get().append(obtain));
			if (usage != null)
				list.add(GLLang.USAGE.get().append(usage));
		} else {
			if (obtain != null)
				list.add(GLLang.OBTAIN.get().append(GLLang.UNKNOWN.get()));
			if (usage != null)
				list.add(GLLang.USAGE.get().append(GLLang.UNKNOWN.get()));
		}
	}

}
