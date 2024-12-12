package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.BlockPos;

import java.util.UUID;

public class InfoUpdateClientManager {

	public static void requestBed(BlockPos pos) {
		GensokyoLegacy.HANDLER.toServer(new BedRequestToServer(pos));
	}

	public static void handleBedInfo(BedInfoToClient bed) {
		BedInfoClientManager.data = bed;
	}

	public static void requestCharacter(UUID uuid) {
		GensokyoLegacy.HANDLER.toServer(new CharacterRequestToServer(uuid));
	}

	public static void handleCharacterInfo(CharacterInfoToClient bed) {
		CharacterInfoClientManager.data = bed;
	}

	public static void clearCache() {
		BedInfoClientManager.lastTime = 0;
		CharacterInfoClientManager.lastTime = 0;
	}
}
