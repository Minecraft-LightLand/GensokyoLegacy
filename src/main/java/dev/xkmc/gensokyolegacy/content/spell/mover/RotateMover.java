package dev.xkmc.gensokyolegacy.content.spell.mover;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.danmakuapi.content.spell.mover.DanmakuMover;
import dev.xkmc.danmakuapi.content.spell.mover.MoverInfo;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.phys.Vec3;

@SerialClass
public final class RotateMover extends DanmakuMover {

	@SerialField
	private Vec3 dir;
	@SerialField
	private double rate;

	@Deprecated
	public RotateMover() {

	}

	public RotateMover(Vec3 dir, double rate) {
		this.dir = dir;
		this.rate = rate;
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		var forward = DanmakuHelper.getOrientation(dir).rotateDegrees(rate * info.tick());
		return new ProjectileMovement(Vec3.ZERO, ProjectileMovement.of(forward).rot());
	}

}
