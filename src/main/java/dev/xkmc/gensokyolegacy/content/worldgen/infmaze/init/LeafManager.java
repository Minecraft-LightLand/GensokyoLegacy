package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init;

import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.dim3d.MazeCell3D;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomState;

import javax.annotation.Nullable;
import java.util.Random;

public interface LeafManager {

	@Nullable
	CellContent getLeaf(RandomSource random, MazeCell3D cell);

	void decoratePath(RandomState random, MazeCell3D cell);

}
