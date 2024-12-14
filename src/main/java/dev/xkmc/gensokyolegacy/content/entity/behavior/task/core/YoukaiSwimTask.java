package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import com.google.common.collect.ImmutableMap;
import dev.xkmc.gensokyolegacy.content.entity.youkai.SmartYoukaiEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.behavior.Behavior;

public class YoukaiSwimTask extends Behavior<SmartYoukaiEntity> {
	private final float chance;

	public YoukaiSwimTask(float chance) {
		super(ImmutableMap.of());
		this.chance = chance;
	}

	public static boolean shouldSwim(SmartYoukaiEntity mob) {
		return !mob.navCtrl.isFlying() && mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold() ||
				mob.isInLava() || mob.isInFluidType((fluidType, height) -> mob.canSwimInFluidType(fluidType) && height > mob.getFluidJumpThreshold());
	}

	protected boolean checkExtraStartConditions(ServerLevel level, SmartYoukaiEntity owner) {
		return shouldSwim(owner);
	}

	protected boolean canStillUse(ServerLevel level, SmartYoukaiEntity entity, long gameTime) {
		return this.checkExtraStartConditions(level, entity);
	}

	protected void tick(ServerLevel level, SmartYoukaiEntity owner, long gameTime) {
		if (owner.getRandom().nextFloat() < this.chance) {
			owner.getJumpControl().jump();
		}
	}
}
