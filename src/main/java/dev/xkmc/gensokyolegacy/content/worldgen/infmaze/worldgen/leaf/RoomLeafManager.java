package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.leaf;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.LeafManager;
import dev.xkmc.l2core.util.MathHelper;
import net.minecraft.world.level.levelgen.RandomState;

import javax.annotation.Nullable;
import java.util.*;

public class RoomLeafManager implements LeafManager {

	private final Map<LeafType, List<CellContentEntry>> map = new TreeMap<>();

	@Nullable
	@Override
	public CellContent getLeaf(Random random, MazeCell3D cell) {
		List<CellContentEntry> list = map.get(LeafType.of(cell));
		if (list == null) return null;
		CellContentEntry entry = MathHelper.pick(list, CellContentEntry::weight, random.nextDouble());
		if (entry == null) return null;
		return entry.content();
	}

	@Override
	public void decoratePath(RandomState random, MazeCell3D cell) {

	}

	public void addCell(@Nullable CellContent cell, LeafType type, int weight) {
		map.computeIfAbsent(type, e -> new ArrayList<>()).add(new CellContentEntry(cell, weight));
	}

}
