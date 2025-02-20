package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.pos.BoundBox;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public record SimpleRoomContent(BlockState floor, BlockState ceiling) implements CellContent {

	public static final MapCodec<SimpleRoomContent> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BlockState.CODEC.fieldOf("floor").forGetter(SimpleRoomContent::floor),
			BlockState.CODEC.fieldOf("ceiling").forGetter(SimpleRoomContent::ceiling)
	).apply(i, SimpleRoomContent::new));

	@Override
	public MapCodec<SimpleRoomContent> codec() {
		return CODEC;
	}

	@Override
	public void generate(BoundBox cell, BoundBox box, ChunkAccess access, RandomSource random) {
		var area = cell.inflate(-1, -1, -1).intersect(box);
		if (area.size() == 0) return;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		if (cell.p1().y() - 2 < box.p1().y()) {
			for (long x = area.p0().x(); x < area.p1().x(); x++) {
				for (long z = area.p0().z(); z < area.p1().z(); z++) {
					pos.set(x, cell.p1().y() - 2, z);
					access.setBlockState(pos, ceiling, false);
				}
			}
		}
		if (cell.p0().y() + 1 >= box.p0().y()) {
			for (long x = area.p0().x(); x < area.p1().x(); x++) {
				for (long z = area.p0().z(); z < area.p1().z(); z++) {
					pos.set(x, cell.p0().y() + 1, z);
					access.setBlockState(pos, floor, false);
				}
			}
		}
	}

}
