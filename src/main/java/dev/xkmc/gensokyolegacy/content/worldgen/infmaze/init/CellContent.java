package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init;

import com.mojang.serialization.MapCodec;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.BoundBox;
import dev.xkmc.gensokyolegacy.init.registrate.GLWorldGen;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkAccess;

public interface CellContent {

	MapCodec<CellContent> CODEC = GLWorldGen.CELL.get().byNameCodec()
			.dispatchMap(CellContent::codec, e -> e);


	void generate(BoundBox cell, BoundBox box, ChunkAccess access, RandomSource random);

	MapCodec<? extends CellContent> codec();


}
