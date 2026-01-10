package dev.xkmc.gensokyolegacy.content.client.structure;

import dev.xkmc.gensokyolegacy.content.attachment.chunk.HomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.IHomeHolder;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public record StructureBoundUpdateToClient(
		StructureKey key, Box structure, Box house, Box room
) implements SerialPacketBase<StructureBoundUpdateToClient> {

	public static void clickBlockInServer(Player player, BlockPos pos) {
		if (!(player instanceof ServerPlayer sp)) return;
		var home = IHomeHolder.find(sp.serverLevel(), pos);
		if (home == null || !home.isValid()) return;
		GensokyoLegacy.HANDLER.toClientPlayer(home.toBoundPacket(), sp);
	}

	public StructureBoundUpdateToClient(StructureKey key, BoundingBox structure, BoundingBox house, BoundingBox room) {
		this(key, Box.of(structure), Box.of(house), Box.of(room));
	}

	public record Box(int x0, int y0, int z0, int x1, int y1, int z1) {

		public static Box of(BoundingBox box) {
			return new Box(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
		}

		public BoundingBox toBox() {
			return new BoundingBox(x0, y0, z0, x1, y1, z1);
		}

	}

	@Override
	public void handle(Player player) {
		StructureInfoClientManager.setStructure(this);
	}

}
