package dev.xkmc.gensokyolegacy.content.block.ritual;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public record RitualResult(List<BlockState> totems, int mana, int instability) {

	public static final RitualResult EMPTY = new RitualResult(List.of(), 0, 0);

	public static RitualResult of(BlockPos center, List<BlockState> states, List<BlockPos> ans, int totalMana) {
		double[] angle = new double[ans.size()];
		for (int i = 0; i < ans.size(); i++) {
			var p = ans.get(i).subtract(center);
			angle[i] = Math.atan2(p.getZ(), p.getX()) * Mth.RAD_TO_DEG;
		}
		Arrays.sort(angle);
		double sum = 0;
		double sep = 360d / angle.length;
		for (int i = 0; i < angle.length; i++) {
			double prev = i > 0 ? angle[i - 1] : angle[angle.length - 1] - 360;
			double diff = angle[i] - prev - sep;
			sum += diff * diff;
		}
		int inst = (int) Math.round(sum / angle.length);
		return new RitualResult(states, totalMana - inst, inst);
	}

}
