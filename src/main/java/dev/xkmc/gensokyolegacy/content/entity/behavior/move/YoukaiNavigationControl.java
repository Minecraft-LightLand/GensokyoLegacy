package dev.xkmc.gensokyolegacy.content.entity.behavior.move;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class YoukaiNavigationControl {

	private final YoukaiEntity self;
	private final CombatFlyingControl combat;
	private final NavigationDebugger debugger;
	private final MoveControl walkCtrl;
	private final MoveControl flyCtrl;
	private final Ground walkNav;
	private final Flying flyNav;

	private boolean isFlying = false;

	public YoukaiNavigationControl(YoukaiEntity self) {
		this.self = self;
		this.combat = new CombatFlyingControl(self);
		this.debugger = new NavigationDebugger(self);
		this.walkCtrl = new MoveControl(self);
		this.walkNav = new Ground(self, self.level());
		this.flyCtrl = new FlyingMoveControl(self, 10, false);
		this.flyNav = new Flying(self, self.level());
		self.setControl(walkCtrl, walkNav);
	}

	public final void setFlying() {
		flyNav.tempFly = false;
		self.setNoGravity(true);
		if (isFlying) return;
		self.getNavigation().stop();
		self.setControl(flyCtrl, flyNav);
		isFlying = true;
	}

	public final void setWalking() {
		flyNav.tempFly = false;
		self.setNoGravity(false);
		if (!isFlying) return;
		self.getNavigation().stop();
		self.setYya(0);
		self.setXxa(0);
		self.setZza(0);
		self.setControl(walkCtrl, walkNav);
		isFlying = false;
	}

	public final boolean isFlying() {
		return isFlying;
	}

	public void tickMove() {
		if (!self.onGround() && self.getDeltaMovement().y < 0.0D) {
			tickFalling();
		}
		LivingEntity target = self.getTarget();
		if (target != null && self.canAttack(target) && isFlying) {
			combat.tickCombatFlying(target);
		}
		if (target == null && isFlying && !flyNav.isDone()) {
			//debugger.debugPath();
		}
	}

	private void tickFalling() {
		if (isFlying && flyNav.tempFly) return;
		if (!isFlying && !walkNav.isDone() && !walkNav.isStuck()) return;
		double fall = self.isAggressive() ? 0.6 : 0.8;
		self.setDeltaMovement(self.getDeltaMovement().multiply(1.0D, fall, 1.0D));
	}

	public void markHuman() {
		walkNav.setCanPassDoors(true);
		walkNav.setCanOpenDoors(true);
		walkNav.setCanFloat(true);

		flyNav.setCanPassDoors(true);
		flyNav.setCanOpenDoors(true);
		flyNav.setCanFloat(true);
	}

	public class Ground extends GroundPathNavigation {

		public Ground(Mob mob, Level level) {
			super(mob, level);
		}

		@Override
		public boolean moveTo(double x, double y, double z, int accuracy, double speed) {
			boolean ans = super.moveTo(x, y, z, accuracy, speed);
			if (ans) return true;
			if (!(isDone() || isStuck())) return false;
			self.setFlying();
			flyNav.tempFly = true;
			return flyNav.moveTo(x, y, z, accuracy, speed);
		}

		@Override
		public boolean moveTo(Entity entity, double speed) {
			boolean ans = super.moveTo(entity, speed);
			if (ans) return true;
			if (!(isDone() || isStuck())) return false;
			self.setFlying();
			flyNav.tempFly = true;
			return flyNav.moveTo(entity, speed);
		}

		@Override
		public void tick() {
			super.tick();
		}

	}

	public class Flying extends FlyingPathNavigation {

		private boolean tempFly = false;

		public Flying(Mob mob, Level level) {
			super(mob, level);
		}

		@Override
		public void tick() {
			super.tick();
			if (tempFly) {
				if (isDone() || isStuck()) {
					setWalking();
				}
			}
		}

	}

}
