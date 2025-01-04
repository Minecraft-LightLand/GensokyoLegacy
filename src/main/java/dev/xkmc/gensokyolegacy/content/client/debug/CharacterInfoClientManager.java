package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class CharacterInfoClientManager {

	static long lastTime = 0;
	static YoukaiEntity lastEntity = null;
	static CharacterInfoToClient data;

	public static void tooltip(List<Component> lines, long gameTime, YoukaiEntity youkai) {
		if (lastEntity != youkai) {
			lastTime = 0;
			data = null;
			lastEntity = youkai;
		}
		if (gameTime > lastTime + 10) {
			lastTime = gameTime;
			InfoUpdateClientManager.requestCharacter(youkai.getUUID());
		}
		if (data == null) {
			lines.add(GLLang.INFO$LOADING.get().withStyle(ChatFormatting.GRAY));
			return;
		}
		lines.addAll(data.info());
		if (Minecraft.getInstance().options.advancedItemTooltips) {
			lines.addAll(data.advanced());
		}
	}
}
