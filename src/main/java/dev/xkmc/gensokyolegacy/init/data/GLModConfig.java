package dev.xkmc.gensokyolegacy.init.data;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.util.ConfigInit;
import net.neoforged.neoforge.common.ModConfigSpec;

public class GLModConfig {

	public static class Server extends ConfigInit {

		public final ModConfigSpec.IntValue higiHealingPeriod;
		public final ModConfigSpec.DoubleValue fairyHealingFactor;

		public final ModConfigSpec.DoubleValue mistletoeSpreadChance;
		public final ModConfigSpec.BooleanValue mistletoeSpreadToPersistentLeaves;

		public final ModConfigSpec.IntValue frogEatCountForHat;
		public final ModConfigSpec.IntValue frogEatRaiderVillagerSightRange;
		public final ModConfigSpec.IntValue frogEatRaiderVillagerNoSightRange;
		public final ModConfigSpec.BooleanValue koishiAttackEnable;
		public final ModConfigSpec.IntValue koishiAttackCoolDown;
		public final ModConfigSpec.DoubleValue koishiAttackChance;
		public final ModConfigSpec.IntValue koishiAttackDamage;
		public final ModConfigSpec.IntValue koishiAttackBlockCount;

		public final ModConfigSpec.DoubleValue danmakuMinPHPDamage;
		public final ModConfigSpec.DoubleValue danmakuPlayerPHPDamage;
		public final ModConfigSpec.DoubleValue danmakuHealOnHitTarget;
		public final ModConfigSpec.BooleanValue enableExtraCoolDown;

		public final ModConfigSpec.BooleanValue exRumiaConversion;
		public final ModConfigSpec.BooleanValue rumiaDamageCap;
		public final ModConfigSpec.BooleanValue rumiaNoTargetHealing;
		public final ModConfigSpec.BooleanValue rumiaHairbandDrop;

		public final ModConfigSpec.BooleanValue reimuSummonFlesh;
		public final ModConfigSpec.BooleanValue reimuSummonKill;
		public final ModConfigSpec.IntValue reimuSummonCost;

		public final ModConfigSpec.BooleanValue fairyAttackYoukaified;
		public final ModConfigSpec.DoubleValue fairySummonReinforcement;

		Server(Builder builder) {
			markL2();
			builder.push("food_effect", "Potion Effects");
			{
				higiHealingPeriod = builder.text("Higi Healing Interval")
						.defineInRange("higiHealingPeriod", 60, 0, 10000);
				fairyHealingFactor = builder.text("Fairy Healing Factor")
						.defineInRange("fairyHealingFactor", 2d, 1, 100);
			}
			builder.pop();

			builder.push("blocks", "Blocks");
			{
				mistletoeSpreadChance = builder.text("Mistletoe spread chance")
						.defineInRange("mistletoeSpreadChance", 0.2, 0, 1);
				mistletoeSpreadToPersistentLeaves = builder.text("Whether mistletoe can spread to persistent leaves")
						.define("mistletoeSpreadToPersistentLeaves", true);
			}
			builder.pop();

			builder.push("suwako_hat", "Suwako Hat");
			{
				frogEatCountForHat = builder.text("Number of raiders with different types frogs need to eat in front of villager to drop Suwako hat")
						.defineInRange("frogEatCountForHat", 3, 1, 10);
				frogEatRaiderVillagerSightRange = builder.text("Range for villagers with direct sight when frog eat raiders")
						.defineInRange("frogEatRaiderVillagerSightRange", 20, 1, 64);
				frogEatRaiderVillagerNoSightRange = builder.text("Range for villagers without direct sight when frog eat raiders")
						.defineInRange("frogEatRaiderVillagerNoSightRange", 10, 1, 64);
			}
			builder.pop();

			builder.push("koishi_attack", "Koishi Attack");
			{
				koishiAttackEnable = builder.text("Enable koishi attack when player has youkaifying or youkaified effect")
						.define("koishiAttackEnable", true);
				koishiAttackCoolDown = builder.text("Time in ticks for minimum time between koishi attacks")
						.defineInRange("koishiAttackCoolDown", 6000, 1, 1000000);
				koishiAttackChance = builder.text("Chance every tick to do koishi attack")
						.defineInRange("koishiAttackChance", 0.001, 0, 1);
				koishiAttackDamage = builder.text("Koishi attack damage")
						.defineInRange("koishiAttackDamage", 100, 0, 100000000);
				koishiAttackBlockCount = builder.text("Number of times player needs to consecutively block Koishi attack to get hat")
						.defineInRange("koishiAttackBlockCount", 3, 0, 100);
			}
			builder.pop();

			builder.push("danmaku_battle", "Danmaku Battle");
			{
				danmakuMinPHPDamage = builder.text("Minimum damage youkai danmaku will deal against non-player")
						.defineInRange("danmakuMinPHPDamage", 0.02, 0, 1);
				danmakuPlayerPHPDamage = builder.text("Minimum damage youkai danmaku will deal against player")
						.defineInRange("danmakuPlayerPHPDamage", 0.1, 0, 1);
				danmakuHealOnHitTarget = builder.text("When danmaku hits target, heal youkai health by percentage of max health")
						.defineInRange("danmakuHealOnHitTarget", 0.2, 0, 1);
				enableExtraCoolDown = builder.text("Extra Damage Cooldown")
						.comment("Enable extra damage cool down on some youkai")
						.define("enableExtraCoolDown", true);
			}
			builder.pop();

			builder.push("rumia", "Rumia");
			{
				exRumiaConversion = builder.text("Enable Ex Rumia conversion when Rumia takes too high damage in one hit")
						.define("exRumiaConversion", true);
				rumiaDamageCap = builder.text("Allow Rumia to cap incoming damage at a factor of max health")
						.define("rumiaDamageCap", true);
				rumiaNoTargetHealing = builder.text("Enable Rumia healing when having no target")
						.define("rumiaNoTargetHealing", true);
				rumiaHairbandDrop = builder.text("Enable Ex Rumia hairband drop")
						.define("rumiaHairbandDrop", true);
			}
			builder.pop();

			builder.push("reimu", "Reimu");
			{
				reimuSummonFlesh = builder.text("Summon Reimu when player eats flesh in front of villagers")
						.define("reimuSummonFlesh", true);
				reimuSummonKill = builder.text("Summon Reimu when player with youkaified/fying effect kills villager in front of other villagers")
						.define("reimuSummonKill", true);
				reimuSummonCost = builder.text("Cost of emerald/gold to summon Reimu")
						.defineInRange("reimuSummonCost", 8, 1, 100000);
			}
			builder.pop();

			builder.push("fairy", "Fairy");
			{
				fairyAttackYoukaified = builder.text("Fairies will actively attack players with youkaifying/ed effects")
						.define("fairyAttackYoukaified", true);
				fairySummonReinforcement = builder.text("Chance for fairies to summon other fairies when killed by non-danmaku damage")
						.defineInRange("fairySummonReinforcement", 0.5, 0, 1);
			}
			builder.pop();
		}

	}

	public static final Server SERVER = GensokyoLegacy.REGISTRATE.registerSynced(Server::new);

	/**
	 * Registers any relevant listeners for config
	 */
	public static void init() {
	}


}
