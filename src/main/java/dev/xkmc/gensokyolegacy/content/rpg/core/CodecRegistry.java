package dev.xkmc.gensokyolegacy.content.rpg.core;

import dev.xkmc.gensokyolegacy.content.rpg.action.CompleteQuestAction;
import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
import dev.xkmc.gensokyolegacy.content.rpg.action.StartQuestAction;
import dev.xkmc.gensokyolegacy.content.rpg.condition.HasAdvancementCondition;
import dev.xkmc.gensokyolegacy.content.rpg.condition.OtherReputationCondition;
import dev.xkmc.gensokyolegacy.content.rpg.condition.SelfReputationCondition;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogStarter;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.SimpleDialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestCondition;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestReward;
import dev.xkmc.gensokyolegacy.content.rpg.requirement.KillMobRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.requirement.KillMobTagRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.requirement.SubmitItemRequirement;
import dev.xkmc.gensokyolegacy.content.rpg.reward.ExpReward;
import dev.xkmc.gensokyolegacy.content.rpg.reward.LootTableReward;
import dev.xkmc.gensokyolegacy.content.rpg.reward.ReputationReward;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.datapack.DatapackReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;

public class CodecRegistry {

	public static final DatapackReg<Dialog> DIALOG = GensokyoLegacy.REG.dataReg("dialog", Dialog.CODEC);
	public static final DatapackReg<DialogStarter> STARTER = GensokyoLegacy.REG.dataReg("dialog_starter", DialogStarter.CODEC);
	public static final DatapackReg<Quest> QUEST = GensokyoLegacy.REG.dataReg("quest", Quest.CODEC);

	public static final CodecRegistryInstance<DialogOption<?>> OPTION = CodecRegistryInstance.of("option");
	public static final CodecRegistryInstance<DialogAction<?>> ACTION = CodecRegistryInstance.of("action");
	public static final CodecRegistryInstance<QuestCondition<?>> CONDITION = CodecRegistryInstance.of("condition");
	public static final CodecRegistryInstance<QuestRequirement<?>> REQUIREMENT = CodecRegistryInstance.of("requirement");
	public static final CodecRegistryInstance<QuestReward<?>> REWARD = CodecRegistryInstance.of("reward");

	public static final CdcVal<SimpleDialogOption> SIMPLE_OPTION = OPTION.reg("simple", SimpleDialogOption.CODEC);

	public static final CdcVal<StartQuestAction> START_QUEST = ACTION.reg("start_quest", StartQuestAction.CODEC);
	public static final CdcVal<CompleteQuestAction> COMPLETE_QUEST = ACTION.reg("complete_quest", CompleteQuestAction.CODEC);

	public static final CdcVal<HasAdvancementCondition> HAS_ADV = CONDITION.reg("has_advancement", HasAdvancementCondition.CODEC);
	public static final CdcVal<SelfReputationCondition> SELF_REP = CONDITION.reg("self_reputation", SelfReputationCondition.CODEC);
	public static final CdcVal<OtherReputationCondition> OTHER_REP = CONDITION.reg("other_reputation", OtherReputationCondition.CODEC);

	public static final CdcVal<KillMobRequirement> KILL_MOB_REQ = REQUIREMENT.reg("kill_mob", KillMobRequirement.CODEC);
	public static final CdcVal<KillMobTagRequirement> KILL_TAG_REQ = REQUIREMENT.reg("kill_tag", KillMobTagRequirement.CODEC);
	public static final CdcVal<SubmitItemRequirement> SUBMIT_ITEM_REQ = REQUIREMENT.reg("submit_item", SubmitItemRequirement.CODEC);

	public static final CdcVal<LootTableReward> LOOT_REWARD = REWARD.reg("loot_table", LootTableReward.CODEC);
	public static final CdcVal<ExpReward> EXP_REWARD = REWARD.reg("exp", ExpReward.CODEC);
	public static final CdcVal<ReputationReward> REPUTATION_REWARD = REWARD.reg("reputation", ReputationReward.CODEC);

	public static void register() {

	}

}
