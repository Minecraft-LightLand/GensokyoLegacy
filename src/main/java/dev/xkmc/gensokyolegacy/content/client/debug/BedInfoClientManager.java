package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.block.bed.YoukaiBedBlockEntity;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class BedInfoClientManager {

	private static long lastTime = 0;
	private static YoukaiBedBlockEntity lastBed = null;
	static BedInfoToClient data;

	public static void tooltip(List<Component> lines, long gameTime, YoukaiBedBlockEntity bed) {
		if (lastBed != bed) {
			lastTime = 0;
			data = null;
			lastBed = bed;
		}
		if (gameTime > lastTime + 20) {
			lastTime = gameTime;
			InfoUpdateClientManager.requestBed(bed.getBlockPos());
		}
		if (data == null) {
			lines.add(GLLang.INFO_LOADING.get().withStyle(ChatFormatting.GRAY));
		} else if (data.key() == null) {
			lines.add(GLLang.INFO_BED_UNBOUND.get().withStyle(ChatFormatting.RED));
		} else if (data.entityPos() != null) {
			var p = data.entityPos();
			lines.add(GLLang.INFO_BED_PRESENT.get(p.getX(), p.getY(), p.getZ()).withStyle(ChatFormatting.GRAY));
		} else if (data.respawnTime() > 0) {
			long diff = data.respawnTime() - gameTime;
			if (diff < 0) diff = 0;
			int sec = (int) ((diff / 20) % 60);
			int min = (int) ((diff / 1200) % 60);
			int hrs = (int) (diff / 72000);
			var str = "%d:%02d:%02d".formatted(hrs, min, sec);
			lines.add(GLLang.INFO_BED_RESPAWN.get(str).withStyle(ChatFormatting.GRAY));
		} else {
			long diff = gameTime - data.lastEntityTime();
			if (diff < 0) diff = 0;
			int sec = (int) ((diff / 20) % 60);
			int min = (int) ((diff / 1200) % 60);
			int hrs = (int) (diff / 72000);
			var str = "%d:%02d:%02d".formatted(hrs, sec, min);
			lines.add(GLLang.INFO_BED_MISSING.get(str).withStyle(ChatFormatting.GRAY));
		}

	}

}
