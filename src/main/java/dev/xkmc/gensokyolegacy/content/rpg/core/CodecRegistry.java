package dev.xkmc.gensokyolegacy.content.rpg.core;

import dev.xkmc.gensokyolegacy.content.rpg.action.DialogAction;
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

	public static final CdcVal<HasAdvancementCondition> HAS_ADV = CONDITION.reg("has_advancement", HasAdvancementCondition.CODEC);
	public static final CdcVal<SelfReputationCondition> SELF_REP = CONDITION.reg("self_reputation", SelfReputationCondition.CODEC);
	public static final CdcVal<OtherReputationCondition> OTHER_REP = CONDITION.reg("other_reputation", OtherReputationCondition.CODEC);

	public static void register() {

	}

}
