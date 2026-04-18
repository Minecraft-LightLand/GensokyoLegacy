package dev.xkmc.gensokyolegacy.content.item.tool;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface InvClickItem {
	void handleClick(ServerPlayer sp, ItemStack stack);
}
