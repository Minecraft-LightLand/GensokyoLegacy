package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.home.core.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.custom.CustomHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.home.custom.RoomVerifier;
import dev.xkmc.gensokyolegacy.content.attachment.index.IndexStorage;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public record StructureEditToServer(
		StructureKey key,
		BlockPos pos,
		StructureEditToServer.Edit edit
) implements SerialPacketBase<StructureEditToServer> {

	public enum Edit {
		SCAN, DELETE
	}

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		var home = IHomeHolder.of(sp.serverLevel(), key);
		if (!(home instanceof CustomHomeHolder data)) return;
		if (edit == Edit.SCAN) {
			var box = new RoomVerifier(sp.serverLevel(), sp).run(pos);
			if (box != null)
				data.data().setBound(data.data().getRoot(), box);
		} else {
			data.attachment().custom.remove(key.pos());
			IndexStorage.get(sp.serverLevel()).remove(key);
		}
	}

}
