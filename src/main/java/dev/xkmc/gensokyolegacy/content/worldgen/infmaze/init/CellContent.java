package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.BoundBox;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.RandomState;

public interface CellContent {

	void generate(RandomState random, BoundBox boxC, ChunkAccess access);

}
