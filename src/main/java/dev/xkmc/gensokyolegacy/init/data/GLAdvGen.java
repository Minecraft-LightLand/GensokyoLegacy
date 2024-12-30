package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import dev.xkmc.gensokyolegacy.compat.food.reg.GLFood;
import dev.xkmc.gensokyolegacy.compat.food.reg.GLFoodItems;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLCriteriaTriggers;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.l2core.serial.advancements.AdvancementGenerator;
import dev.xkmc.l2core.serial.advancements.CriterionBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Optional;

public class GLAdvGen {

	public static final ResourceLocation FLESH_WARN = GensokyoLegacy.loc("flesh_warn");
	public static final ResourceLocation HURT_WARN = GensokyoLegacy.loc("hurt_warn");
	public static final ResourceLocation KOISHI_FIRST = GensokyoLegacy.loc("koishi_first");

	public static void genAdv(RegistrateAdvancementProvider pvd) {
		pvd.accept(Advancement.Builder.advancement().addCriterion("flesh_warn",
				GLCriteriaTriggers.FLESH_WARN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
		).build(FLESH_WARN));
		pvd.accept(Advancement.Builder.advancement().addCriterion("hurt_warn",
				GLCriteriaTriggers.HURT_WARN.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
		).build(HURT_WARN));
		pvd.accept(Advancement.Builder.advancement().addCriterion("koishi_first",
				GLCriteriaTriggers.KOISHI_FIRST.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))
		).build(KOISHI_FIRST));

		var gen = new AdvancementGenerator(pvd, GensokyoLegacy.MODID);
		var b = gen.new TabBuilder("main");
		var root = b.root("welcome_to_youkais_homecoming", GLItems.SUWAKO_HAT.asStack(),
				CriterionBuilder.one(PlayerTrigger.TriggerInstance.tick()),
				"Youkai's Homecoming", "Welcome To Youkai's Homecoming");


		var youkai = root.create("flesh", GLFood.FLESH.item.asStack(),
						CriterionBuilder.item(GLFood.FLESH.item.get()),
						"Where is it from?", "Get weird meat")
				.type(AdvancementType.GOAL, true, true, false);
		youkai.create("bloody", GLItems.BLOOD_BOTTLE.asStack(1),
						CriterionBuilder.item(GLItems.BLOOD_BOTTLE.item.get()),
						"Bloody!", "Get a Blood Bottle")
				.create("monstrosity", GLFoodItems.FLESH_FEAST.asStack(),
						CriterionBuilder.one(ConsumeItemTrigger.TriggerInstance.usedItem(GLFood.BOWL_OF_FLESH_FEAST.item.get())),
						"Monstrosity", "Eat a bowl of flesh feast")
				.type(AdvancementType.GOAL, true, true, false);

		youkai.create("mary_call", ModItems.IRON_KNIFE.get(),
						CriterionBuilder.one(GLCriteriaTriggers.KOISHI_RING.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
						"It’s Mary-san", "Receives a phone call from Koishi in the nether in Youkaifying effect")
				.create("koishi_hat", GLItems.KOISHI_HAT.get(),
						CriterionBuilder.item(GLItems.KOISHI_HAT.get()),
						"Koishi's Hat", "Obtain Koishi's Hat after playing with Koishi")
				.type(AdvancementType.CHALLENGE, true, true, false);
		youkai.create("suwako_wear", GLItems.STRAW_HAT.get(),
						CriterionBuilder.one(GLCriteriaTriggers.SUWAKO_WEAR.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
						"Godhood Ascension", "In youkaified or youkaifying effect, give the straw hat to a frog")
				.create("suwako_hat", GLItems.SUWAKO_HAT.get(),
						CriterionBuilder.item(GLItems.SUWAKO_HAT.get()),
						"Faith Collection", "Obtain Suwako's Hat after collecting enough faith")
				.type(AdvancementType.CHALLENGE, true, true, false);

		var danmaku = youkai.create("trade_danmaku", GLFood.FLESH_CHOCOLATE_MOUSSE.item.get(),
				CriterionBuilder.one(GLCriteriaTriggers.TRADE.get().createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty()))),
				"Cute Merchant", "In youkaified effect, trade with Rumia to get Danmaku");

		danmaku.create("lost_memories", GLItems.RUMIA_HAIRBAND.get(),
						CriterionBuilder.item(GLItems.RUMIA_HAIRBAND.get()),
						"Lost Memories", "When Rumia takes more than 40 damage, she will convert to Ex. Rumia. Defeat Ex. Rumia with danmaku and obtain Rumia's Hairband.")
				.type(AdvancementType.CHALLENGE);

		danmaku.create("spellcard_power", GLItems.REIMU_SPELL.get(),
						CriterionBuilder.item(GLItems.REIMU_SPELL.get()),
						"Spellcard Power", "When you eat flesh in front of villagers, Reimu will try to exterminate you. Defat Reimu and obtain her spellcard")
				.type(AdvancementType.CHALLENGE);

		/*TODO
		danmaku.create("feed_reimu", GLItems.REIMU_HAIRBAND.get(),
						Util.make(CriterionBuilder.and(), c -> Streams.concat(
										Arrays.stream(YHDish.values()).filter(e -> !e.isFlesh()).map(e -> e.block.get()),
										Arrays.stream(YHSake.values()).filter(e -> !e.isFlesh()).map(e -> e.item.get()),
										Arrays.stream(YHCoffee.values()).map(e -> e.item.get()),
										Arrays.stream(YHFood.values()).filter(YHFood::isReimuFood).map(e -> e.item.get()))
								.map(e -> Pair.of(e, FeedReimuTrigger.usedItem(e)))
								.forEach(p -> c.add(BuiltInRegistries.ITEM.getKey(p.getFirst().asItem()).toString(), p.getSecond()))),
						"Satisfied Reimu", "Feed Reimu all appealing food from Youkai's Homecoming to make her happy and give you her hairband")
				.add(new RewardBuilder(YoukaisHomecoming.REGISTRATE, 0, YoukaisHomecoming.loc("reimu_reward"), () ->
						LootTable.lootTable().withPool(LootPool.lootPool().add(
								LootTableTemplate.getItem(GLItems.REIMU_HAIRBAND.get(), 1, 1)))));
*/
		root.finish();
	}

}
