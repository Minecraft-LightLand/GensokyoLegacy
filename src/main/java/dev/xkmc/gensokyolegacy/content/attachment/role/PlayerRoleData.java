package dev.xkmc.gensokyolegacy.content.attachment.role;

import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;

@SerialClass
public class PlayerRoleData {

	@SerialField
	private int progress;

	public int getProgress() {
		return progress;
	}

	public void advance(int maxPoint, int val) {
		if (progress < maxPoint)
			progress = Math.min(maxPoint, progress + val);
	}

	public void retract(int val) {
		progress = Math.max(0, progress - val);
	}

}
