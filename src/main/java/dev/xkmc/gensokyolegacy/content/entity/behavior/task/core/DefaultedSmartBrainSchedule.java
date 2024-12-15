package dev.xkmc.gensokyolegacy.content.entity.behavior.task.core;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.schedule.SmartBrainSchedule;

public class DefaultedSmartBrainSchedule extends SmartBrainSchedule {

	@Override
	public Activity tick(LivingEntity self) {
		var ans = super.tick(self);
		if (ans == null || !self.getBrain().activityRequirementsAreMet(ans)) {
			return Activity.IDLE;
		}
		return ans;
	}

}
