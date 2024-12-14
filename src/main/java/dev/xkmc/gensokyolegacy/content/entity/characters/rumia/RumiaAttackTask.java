package dev.xkmc.gensokyolegacy.content.entity.characters.rumia;

import dev.xkmc.danmakuapi.content.entity.DanmakuHelper;
import dev.xkmc.gensokyolegacy.content.entity.behavior.task.combat.YoukaiAttackTask;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.List;

public class RumiaAttackTask extends YoukaiAttackTask<RumiaEntity> {

	private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(4)
			.hasMemory(MemoryModuleType.ATTACK_TARGET)
			.noMemory(GLBrains.MEM_DOWN.get())
			.usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);

	private static final int BALL_RANGE = 10;
	private static final int APPROACH_RANGE = 16;
	private static final int SHOOT_FREQUENCY = 40;
	private static final int SEPARATION = 12, ANGLE = 3;
	private static final float SPEED = 0.64f, SPEED_VAR = 0.08f;

	public RumiaAttackTask() {
		super(APPROACH_RANGE);
	}

	@Override
	protected boolean checkExtraStartConditions(ServerLevel level, RumiaEntity entity) {
		return !entity.isBlocked() && super.checkExtraStartConditions(level, entity);
	}

	@Override
	protected boolean shouldKeepRunning(RumiaEntity entity) {
		return !entity.isBlocked() && super.shouldKeepRunning(entity);
	}

	@Override
	protected boolean specialAction(RumiaEntity youkai) {
		if (youkai.isCharged()) {
			LivingEntity target = youkai.getTarget();
			if (target != null) {
				boolean sight = youkai.getSensing().hasLineOfSight(target);
				double dist = youkai.distanceToSqr(target);
				if (sight) {
					attack(youkai, target, dist, true);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	protected double getMeleeRange(RumiaEntity youkai) {
		return youkai.isCharged() ? 3 : 2;
	}

	@Override
	protected void attack(RumiaEntity youkai, LivingEntity target, double dist, boolean sight) {
		if (sight && dist < BALL_RANGE * BALL_RANGE) {
			youkai.state.startChargeAttack(target);
		}
		super.attack(youkai, target, dist, sight);
	}

	@Override
	protected void meleeAttack(RumiaEntity youkai, LivingEntity target) {
		if (youkai.doHurtTarget(target)) {
			youkai.state.onAttack(target);
		}
	}

	protected int shoot(RumiaEntity youkai, LivingEntity target, List<LivingEntity> all) {
		if (youkai.isCharged()) return 10;
		int round = youkai.isEx() ? 5 : 3;
		shoot(youkai, target, round);
		double range = getShootRange(youkai);
		for (var e : all) {
			if (e != target && e.distanceToSqr(youkai) < range * range) {
				shoot(youkai, e, round - 2);
			}
		}
		int ans = SHOOT_FREQUENCY;
		if (youkai.isEx()) ans /= 2;
		return ans;
	}

	private void shoot(RumiaEntity youkai, LivingEntity target, int round) {
		double range = getShootRange(youkai);
		double dx = target.getX() - youkai.getX();
		double dy = target.getY(0.5D) - youkai.getY(0.5D);
		double dz = target.getZ() - youkai.getZ();
		var vec = new Vec3(dx, dy, dz).normalize();
		var ori = DanmakuHelper.getOrientation(vec);
		int off = youkai.getRandom().nextFloat() < 0.5 ? -ANGLE : ANGLE;
		float dmg = (float) youkai.getAttributeValue(Attributes.ATTACK_DAMAGE);

		for (int i = 0; i < round; i++) {
			float speed = SPEED + i * SPEED_VAR;
			int angle = off * (round - i - 2);
			int life = (int) (range / speed);
			youkai.shoot(dmg, life, ori.rotateDegrees(-SEPARATION + angle).scale(speed), DyeColor.RED);
			youkai.shoot(dmg, life, ori.rotateDegrees(angle).scale(speed), DyeColor.BLACK);
			youkai.shoot(dmg, life, ori.rotateDegrees(SEPARATION + angle).scale(speed), DyeColor.RED);
		}
	}

}
