package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.client.structure.StructureInfoRequestToServer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.BlockPos;

import java.util.UUID;

public class InfoUpdateClientManager {

	public static void requestBed(BlockPos pos) {
		GensokyoLegacy.HANDLER.toServer(new BlockRequestToServer(pos));
	}

	public static void handleBedInfo(BlockInfoToClient bed) {
		BedInfoClientManager.data = bed;
	}

	public static void requestCharacter(UUID uuid) {
		GensokyoLegacy.HANDLER.toServer(new CharacterRequestToServer(uuid));
	}

	public static void handleCharacterInfo(CharacterInfoToClient bed) {
		CharacterInfoClientManager.data = bed;
	}

	public static void requestStructure(StructureKey key) {
		GensokyoLegacy.HANDLER.toServer(new StructureInfoRequestToServer(key));
	}

	public static void clearCache() {
		BedInfoClientManager.lastTime = 0;
		CharacterInfoClientManager.lastTime = 0;
	}

}
