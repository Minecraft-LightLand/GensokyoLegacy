package dev.xkmc.gensokyolegacy.content.entity.behavior.task.play;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLBrains;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class YoukaiHuntTask extends ExtendedBehaviour<SmartYoukaiEntity> {

	private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = MemoryTest.builder(3)
			.hasMemory(GLBrains.MEM_PREY.get())
			.usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);

	private final int distance;

	private LivingEntity target;

	private int cooldown = 0;

	public YoukaiHuntTask(int distance) {
		this.distance = distance;
	}

	@Override
	protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
		return MEMORY_REQUIREMENTS;
	}

	@Override
	protected boolean shouldKeepRunning(SmartYoukaiEntity entity) {
		return target != null && entity.targets.isValidTarget(target);
	}

	@Override
	protected void start(SmartYoukaiEntity entity) {
		target = BrainUtils.getMemory(entity, GLBrains.MEM_PREY.get());
		cooldown = 0;
		if (target == null) return;
		BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
	}

	@Override
	protected void stop(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		BrainUtils.clearMemory(entity, MemoryModuleType.WALK_TARGET);
		BrainUtils.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
		target = null;
		cooldown = 0;
	}

	@Override
	protected void tick(SmartYoukaiEntity entity) {
		if (target == null) return;
		double dist = entity.distanceTo(target);
		if (dist > distance * 1.5 && !BrainUtils.hasMemory(entity, MemoryModuleType.WALK_TARGET)) {
			BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(target, 1, distance));
		}
		if (dist > distance * 2) return;
		if (cooldown > 0) {
			cooldown--;
			return;
		}
		cooldown = entity.combatManager.doPreyAttack(target);
	}

}
