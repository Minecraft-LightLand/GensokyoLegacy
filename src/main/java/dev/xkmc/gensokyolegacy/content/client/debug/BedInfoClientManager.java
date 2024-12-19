package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.block.base.IDebugInfoBlockEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BedInfoClientManager {

	static long lastTime = 0;
	static IDebugInfoBlockEntity lastBed = null;
	static BlockInfoToClient data;

	public static void tooltip(List<Component> lines, long gameTime, IDebugInfoBlockEntity bed) {
		if (lastBed != bed) {
			lastTime = 0;
			data = null;
			lastBed = bed;
		}
		if (gameTime > lastTime + 10) {
			lastTime = gameTime;
			InfoUpdateClientManager.requestBed(bed.asBlockEntity().getBlockPos());
		}
		if (data == null) {
			lines.add(GLLang.INFO_LOADING.get().withStyle(ChatFormatting.GRAY));
			return;
		}
		lines.addAll(data.info());
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			lines.addAll(data.advanced());
		}
	}

}
