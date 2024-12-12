package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterData;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
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
		if (gameTime > lastTime + 20) {
			lastTime = gameTime;
			InfoUpdateClientManager.requestCharacter(youkai.getUUID());
		}
		if (data == null) {
			lines.add(GLLang.INFO_LOADING.get().withStyle(ChatFormatting.GRAY));
			return;
		}
		if (data.home() == null || data.bed() == null) {
			lines.add(GLLang.INFO_ENTITY_UNBOUND.get().withStyle(ChatFormatting.GRAY));
		} else {
			var p = data.bed();
			lines.add(GLLang.INFO_ENTITY_BED.get(p.getX(), p.getY(), p.getZ()).withStyle(ChatFormatting.GRAY));
		}
		lines.add(GLLang.INFO_ENTITY_REPUTATION.get(data.reputation()).withStyle(
				switch (CharacterData.getState(data.reputation())) {
					case FRIEND -> ChatFormatting.GREEN;
					case STRANGER -> ChatFormatting.WHITE;
					case JERK -> ChatFormatting.YELLOW;
					case ENEMY -> ChatFormatting.RED;
				}
		));
		if (data.feedCD() > 0) {
			lines.add(GLLang.INFO_ENTITY_FEED.time(data.feedCD()).withStyle(ChatFormatting.GRAY));
		}
	}
}
