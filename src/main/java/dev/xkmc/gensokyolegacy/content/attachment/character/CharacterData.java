package dev.xkmc.gensokyolegacy.content.attachment.character;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.content.entity.module.ReputationState;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class CharacterData {

	public static ReputationState getState(int reputation) {
		if (reputation >= 150)
			return ReputationState.FRIEND;
		if (reputation >= -50)
			return ReputationState.STRANGER;
		if (reputation >= -150)
			return ReputationState.JERK;
		return ReputationState.ENEMY;
	}

	@SerialField
	protected final FeedModuleData foodData = new FeedModuleData();

	@SerialField
	public int reputation;

	protected void dailyUpdate() {
		if (reputation > 150)
			reputation--;
		if (reputation < -150)
			reputation++;
	}

	public ReputationState getState() {
		return getState(reputation);
	}

	protected void onKilledByCharacter() {
		if (reputation <= 0) {
			reputation += 20;
		}
	}

	protected void onHurtCharacter(Player player, YoukaiEntity e, float damage, DamageSource source) {
		boolean danmaku = source.is(DanmakuDamageTypes.DANMAKU_TYPE);
		if (danmaku) return;
		boolean first = !e.targets.contains(player) && e.getLastHurtByMob() != player;
		if (first && damage <= 4) {
			if (reputation >= 100)
				reputation -= 1;
			else if (reputation >= 0)
				reputation -= 5;
			else reputation -= 10;
		} else {
			if (first && reputation >= 100)
				reputation -= 5;
			else if (reputation >= 0)
				reputation -= 10;
			else reputation -= 20;
		}
	}

	protected void onKillCharacter() {
		reputation = Math.min(0, reputation - 200);
	}

}
