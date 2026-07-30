package dev.xkmc.gensokyolegacy.content.rpg.action;

import com.mojang.serialization.MapCodec;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;

public record StartQuestAction() implements DialogAction<StartQuestAction> {

	public static final MapCodec<StartQuestAction> CODEC = MapCodec.unit(new StartQuestAction());

	@Override
	public void execute(ActionContext context) {
		var quest = context.quest();
		if (quest.isEmpty()) return;
		GLMeta.QUEST.type().getOrCreate(context.sp()).start(context.sp(), quest.get());
	}

	@Override
	public MapCodec<StartQuestAction> codec() {
		return CODEC;
	}

}
