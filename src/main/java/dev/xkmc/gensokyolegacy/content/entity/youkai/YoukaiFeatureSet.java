package dev.xkmc.gensokyolegacy.content.entity.youkai;

/**
 * @param effectImmune         Immune to mob effects, fire, heal reduction, etc
 * @param damageFilter         Enable damage filter, set health immunity, etc
 * @param damageCoolDown       Enable damage cooldown
 * @param noTargetHealing      Enable no-target healing
 * @param limiter              Damage limit factor (1/n)
 * @param nonDanmakuProtection Damage reduction for non-danmaku protection (1/n)
 */
public record YoukaiFeatureSet(
		boolean effectImmune, boolean damageFilter, boolean damageCoolDown,
		boolean noTargetHealing, boolean trueDamageOnImmune, boolean hasBossBar,
		int limiter, int nonDanmakuProtection, int noPlayerDiscardTime,
		double maxSpeed) {

	public static final YoukaiFeatureSet NONE = YoukaiFeatureSet.builder()
			.limit(5, 1)
			.build();

	public static final YoukaiFeatureSet BOSS = YoukaiFeatureSet.builder()
			.markBoss().limit(20, 1)
			.build();

	public static final YoukaiFeatureSet FULL = YoukaiFeatureSet.builder()
			.markBoss().limit(20, 5).damageFilter().damageCoolDown()
			.build();

	public static final YoukaiFeatureSet SAGE = YoukaiFeatureSet.builder()
			.markBoss().limit(20, 5).damageFilter().damageCoolDown()
			.trueDamageOnImmune()
			.build();

	public static final YoukaiFeatureSet MAIDEN = YoukaiFeatureSet.builder()
			.markBoss().limit(20, 5).damageFilter().damageCoolDown()
			.trueDamageOnImmune().noPlayerTime(30)
			.build();

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {

		private boolean effectImmune = false;
		private boolean damageFilter = false;
		private boolean damageCoolDown = false;
		private boolean noTargetHealing = false;
		private boolean hasBossBar = false;
		private boolean trueDamageOnImmune = false;
		private int limiter = 1;
		private int nonDanmakuProtection = 1;
		private int noPlayerDiscardTime = -1;
		private double maxSpeed = 0.5;

		public Builder markBoss() {
			effectImmune = true;
			noTargetHealing = true;
			hasBossBar = true;
			return this;
		}

		public Builder damageFilter() {
			damageFilter = true;
			return this;
		}

		public Builder damageCoolDown() {
			damageCoolDown = true;
			return this;
		}

		public Builder trueDamageOnImmune() {
			trueDamageOnImmune = true;
			return this;
		}

		public Builder limit(int limit, int protection) {
			limiter = limit;
			nonDanmakuProtection = protection;
			return this;
		}

		public Builder noPlayerTime(int time) {
			noPlayerDiscardTime = time;
			return this;
		}

		public YoukaiFeatureSet build() {
			return new YoukaiFeatureSet(
					effectImmune, damageFilter, damageCoolDown,
					noTargetHealing, trueDamageOnImmune, hasBossBar,
					limiter, nonDanmakuProtection,
					noPlayerDiscardTime, maxSpeed
			);
		}

	}

}
