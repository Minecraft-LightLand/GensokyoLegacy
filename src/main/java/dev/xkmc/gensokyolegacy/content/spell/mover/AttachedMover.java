package dev.xkmc.gensokyolegacy.content.spell.mover;

import dev.xkmc.danmakuapi.content.spell.mover.MoverInfo;
import dev.xkmc.danmakuapi.content.spell.mover.TargetPosMover;
import dev.xkmc.fastprojectileapi.entity.ProjectileMovement;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@SerialClass
public class AttachedMover extends TargetPosMover {

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
		return new ProjectileMovement(pos(info).subtract(info.prevPos()), info.self().self().rot());
	}
}
