package dev.xkmc.gensokyolegacy.content.attachment.character;

import dev.xkmc.danmakuapi.init.data.DanmakuDamageTypes;
import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class CharacterData {

	public static final int MAX = 300, MIN = -300;

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

	public void gainReputation(int val, int max) {
		if (reputation < max) {
			reputation = Math.min(reputation + val, max);
		}
	}

	public void loseReputation(int val, int min) {
		if (reputation > min) {
			reputation = Math.max(reputation - val, min);
		}
	}

	protected void dailyUpdate() {
		loseReputation(1, 150);
		gainReputation(1, -150);
	}

	public ReputationState getState() {
		return getState(reputation);
	}

	protected void onKilledByCharacter() {
		gainReputation(100, -50);
	}

	protected void onHurtCharacter(Player player, YoukaiEntity e, float damage, DamageSource source) {
		boolean danmaku = source.is(DanmakuDamageTypes.DANMAKU_TYPE);
		if (danmaku) return;
		boolean first = !e.targets.contains(player) && e.getLastHurtByMob() != player;
		if (first && damage <= 4) {
			if (reputation >= 100)
				loseReputation(1, 0);
			else if (reputation >= 0)
				loseReputation(5, -100);
			else loseReputation(10, -100);
		} else {
			if (first && reputation >= 100)
				loseReputation(5, 0);
			else if (reputation >= 0)
				loseReputation(10, -150);
			else loseReputation(20, -150);
		}
	}

	protected void onKillCharacter() {
		loseReputation(200, MIN);
	}

}
