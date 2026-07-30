package dev.xkmc.gensokyolegacy.content.rpg.action;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record ActionContext(
		ServerPlayer sp,
		YoukaiEntity character,
		Optional<Holder<Quest>> quest
) {
}
