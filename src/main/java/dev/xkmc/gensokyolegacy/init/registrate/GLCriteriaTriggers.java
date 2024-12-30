package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.trigger.FeedReimuTrigger;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.BuiltInRegistries;

public class GLCriteriaTriggers {

	public static final SR<CriterionTrigger<?>> CT = SR.of(GensokyoLegacy.REG, BuiltInRegistries.TRIGGER_TYPES);

	public static final Val<PlayerTrigger> SUWAKO_WEAR = CT.reg("suwako_wear", PlayerTrigger::new);
	public static final Val<PlayerTrigger> KOISHI_RING = CT.reg("koishi_ring", PlayerTrigger::new);
	public static final Val<PlayerTrigger> TRADE = CT.reg("rumia_trade", PlayerTrigger::new);
	public static final Val<PlayerTrigger> REIMU_HAPPY = CT.reg("udumbara_feed", PlayerTrigger::new);
	public static final Val<PlayerTrigger> FLESH_WARN = CT.reg("flesh_warn", PlayerTrigger::new);
	public static final Val<PlayerTrigger> HURT_WARN = CT.reg("hurt_warn", PlayerTrigger::new);
	public static final Val<PlayerTrigger> KOISHI_FIRST = CT.reg("koishi_first", PlayerTrigger::new);
	public static final Val<FeedReimuTrigger> FEED_REIMU = CT.reg("feed_reimu", FeedReimuTrigger::new);

	public static void register() {

	}


}