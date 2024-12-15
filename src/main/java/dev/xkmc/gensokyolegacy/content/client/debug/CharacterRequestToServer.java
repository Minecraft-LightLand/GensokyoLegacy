package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.index.BedRefData;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.content.entity.module.FeedModule;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record CharacterRequestToServer(UUID id) implements SerialPacketBase<CharacterRequestToServer> {

	@Override
	public void handle(Player player) {
		if (!(player instanceof ServerPlayer sp)) return;
		if (!(sp.serverLevel().getEntity(id) instanceof YoukaiEntity e)) return;
		var home = StructureKey.of(e);
		var bed = home.map(k -> BedRefData.of(sp.serverLevel(), k, e.getType()))
				.map(BedRefData::getBedPos);
		int feedCD = e.getModule(FeedModule.class).map(FeedModule::getCoolDown).orElse(0);
		String activity = e instanceof SmartYoukaiEntity smart ? smart.getBrainDebugInfo() : "";
		e.getData(sp).ifPresent(data -> GensokyoLegacy.HANDLER.toClientPlayer(new CharacterInfoToClient(
				home.orElse(null),
				bed.orElse(null),
				data.data().reputation,
				feedCD, activity
		), sp));
	}

}
