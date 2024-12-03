package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class CombatFlyingControl {

	private static final int GROUND_HEIGHT = 5, ATTEMPT_ABOVE = 3;

	private final YoukaiEntity self;

	public CombatFlyingControl(YoukaiEntity self) {
		this.self = self;
	}

	private void setY(double vy) {
		Vec3 vec3 = self.getDeltaMovement();
		self.setDeltaMovement(vec3.x, vy, vec3.z);
		if (vy > 0 && self.onGround()) {
			self.hasImpulse = true;
		}
	}

	private boolean tooHigh() {
		BlockPos pos = self.getOnPos();
		for (int i = 0; i < GROUND_HEIGHT; i++) {
			BlockPos off = pos.offset(0, -i, 0);
			if (!self.level().getBlockState(off).isAir()) return false;
		}
		return true;
	}

	public void tickCombatFlying(LivingEntity target) {
		boolean tooHigh = tooHigh();
		int expectedHeight = tooHigh ? 0 : ATTEMPT_ABOVE;
		double low = -0.5;
		double high = tooHigh ? 0 : 0.5;
		double diff = target.getEyeY() + expectedHeight - self.getEyeY();
		Vec3 vec3 = self.getDeltaMovement();
		double moveY = vec3.y * 0.5 + diff * 0.02;
		if (self.getEyeY() < target.getEyeY() + expectedHeight + low) {
			setY(Math.max(vec3.y, moveY));
		}
		if (self.getEyeY() > target.getEyeY() + expectedHeight + high) {
			if (diff < -1) {
				setY(Math.min(vec3.y, moveY));
			} else if (tooHigh) {
				setY(Math.min(vec3.y, -0.01));
			}
		}
	}

}
