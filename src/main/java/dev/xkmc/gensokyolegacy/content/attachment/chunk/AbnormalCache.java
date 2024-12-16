package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class AbnormalCache {

	@SerialField
	final IntOpenHashSet air = new IntOpenHashSet();
	@SerialField
	final IntOpenHashSet primary = new IntOpenHashSet();
	@SerialField
	final IntOpenHashSet secondary = new IntOpenHashSet();

	public void addAir(int step) {
		air.add(step);
	}

	public void addPrimary(int step) {
		primary.add(step);
	}

	public void addSecondary(int step) {
		secondary.add(step);
	}

	public int @Nullable [] pop(int count, FixStage stage) {
		if (!air.isEmpty()) {
			return pop(air, count);
		}
		if (stage == FixStage.PATH)
			return null;
		if (!primary.isEmpty()) {
			return pop(primary, count);
		}
		if (stage == FixStage.PRIMARY)
			return null;
		if (!secondary.isEmpty()) {
			return pop(secondary, count);
		}
		return null;
	}

	private int[] pop(IntOpenHashSet set, int count) {
		if (set.size() <= count) {
			int[] all = new int[set.size()];
			int ind = 0;
			for (int e : set) {
				all[ind++] = e;
			}
			return all;
		} else {
			int[] toRemove = new int[count];
			int ind = 0;
			for (int e : set) {
				if (ind >= toRemove.length)
					break;
				toRemove[ind++] = e;
				set.remove(e);
			}
			return toRemove;
		}
	}

}
