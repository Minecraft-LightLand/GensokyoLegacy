package dev.xkmc.gensokyolegacy.content.attachment.gap;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public record GapMapping(
		BlockPos worldPos,
		BlockPos gapPos,
		ResourceLocation dimension
) {

	public GapMapping updateWorld(ServerLevel sl, BlockPos pos) {
		return new GapMapping(pos, gapPos, sl.dimension().location());
	}

	public GapMapping updateGap(ServerLevel sl, BlockPos pos) {
		return new GapMapping(worldPos, pos, dimension);
	}

}
