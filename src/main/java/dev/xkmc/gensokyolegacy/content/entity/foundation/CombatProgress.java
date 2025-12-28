package dev.xkmc.gensokyolegacy.content.entity.foundation;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.world.entity.LivingEntity;

@SerialClass
public class CombatProgress {

	@SerialField
	public float maxProgress;
	@SerialField
	public float progress;
	@SerialField
	public float oldProgress;

	public void init(DamageRefactorEntity e) {
		if (maxProgress <= 0) maxProgress = e.getMaxHealth();
		if (progress <= 0) progress = maxProgress;
	}

	public float getMaxProgress(float def) {
		return maxProgress <= 0 ? def : maxProgress;
	}

	public float getProgress() {
		return progress;
	}

	public void set(LivingEntity e, float amount) {
		progress = amount;
		if (progress != oldProgress && !e.level().isClientSide()) {
			oldProgress = progress;
			GensokyoLegacy.HANDLER.toTrackingPlayers(new CombatToClient(e.getId(), this), e);
		}
	}

	public void setMax(float max) {
		if (max > maxProgress)
			maxProgress = max;
		progress = maxProgress;
	}

}
