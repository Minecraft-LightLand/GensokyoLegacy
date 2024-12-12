package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record CharacterInfoToClient(
		@Nullable StructureKey home,
		@Nullable BlockPos bed,
		int reputation,
		int feedCD
) implements SerialPacketBase<CharacterInfoToClient> {

	@Override
	public void handle(Player player) {
		InfoUpdateClientManager.handleCharacterInfo(this);
	}

}
