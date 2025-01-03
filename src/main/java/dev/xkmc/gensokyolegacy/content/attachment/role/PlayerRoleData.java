package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class PlayerRoleData {

	private static final double MAX = 1000;

	@SerialField
	private int progress;

	public double getProgress() {
		return progress / MAX;
	}

	public void advance(double max, int val) {
		int maxPoint = (int) Math.round(max * MAX);
		if (progress < maxPoint)
			progress = Math.max(maxPoint, progress + val);
	}

	public void retract(int val) {
		progress = Math.max(0, progress - val);
	}

}
