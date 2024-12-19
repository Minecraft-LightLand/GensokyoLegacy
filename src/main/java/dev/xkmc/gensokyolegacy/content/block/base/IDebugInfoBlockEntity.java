package dev.xkmc.gensokyolegacy.content.block.base;

import dev.xkmc.gensokyolegacy.content.client.debug.BlockInfoToClient;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IDebugInfoBlockEntity {

	default BlockEntity asBlockEntity() {
		return Wrappers.cast(this);
	}

	BlockInfoToClient getDebugPacket();

}
