package dev.xkmc.gensokyolegacy.content.spell.mover;

import dev.xkmc.danmakuapi.content.spell.mover.MoverInfo;
import dev.xkmc.danmakuapi.content.spell.mover.TargetPosMover;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class AttachedFreeRotMover extends TargetPosMover {

	@Override
	public Vec3 pos(MoverInfo info) {
		var e = info.self();
		if (e.self().getOwner() instanceof LivingEntity self) {
			return self.position().add(0, self.getBbHeight() / 2, 0);
		}
		return info.prevPos();
	}

	@Override
	public ProjectileMovement move(MoverInfo info) {
		Vec3 rot = info.self().self().rot();
		if (info.self().self().getOwner() instanceof LivingEntity self) {
			rot = new Vec3(self.getViewXRot(1) * Mth.DEG_TO_RAD, self.getViewYRot(1) * Mth.DEG_TO_RAD, 0);
		}
		return new ProjectileMovement(pos(info).subtract(info.prevPos()), rot);
	}

}
