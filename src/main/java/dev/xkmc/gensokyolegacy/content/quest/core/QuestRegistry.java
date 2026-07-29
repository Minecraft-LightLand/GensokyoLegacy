package dev.xkmc.gensokyolegacy.content.quest.core;

import dev.xkmc.gensokyolegacy.content.quest.condition.OtherReputationCondition;
import dev.xkmc.gensokyolegacy.content.quest.condition.HasAdvancementCondition;
import dev.xkmc.gensokyolegacy.content.quest.condition.SelfReputationCondition;
import dev.xkmc.l2core.init.reg.simple.CdcVal;

public class QuestRegistry {

	public static final CodecRegistryInstance<QuestCondition<?>> CONDITION = CodecRegistryInstance.of("condition");
	public static final CodecRegistryInstance<QuestRequirement<?>> REQUIREMENT = CodecRegistryInstance.of("requirement");
	public static final CodecRegistryInstance<QuestReward<?>> REWARD = CodecRegistryInstance.of("reward");

	public static final CdcVal<HasAdvancementCondition> HAS_ADV = CONDITION.reg("has_advancement", HasAdvancementCondition.CODEC);
	public static final CdcVal<SelfReputationCondition> SELF_REP = CONDITION.reg("self_reputation", SelfReputationCondition.CODEC);
	public static final CdcVal<OtherReputationCondition> OTHER_REP = CONDITION.reg("other_reputation", OtherReputationCondition.CODEC);

	public static void register() {

	}

}
