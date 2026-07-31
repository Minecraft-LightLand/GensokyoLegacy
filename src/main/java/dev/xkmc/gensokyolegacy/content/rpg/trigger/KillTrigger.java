package dev.xkmc.gensokyolegacy.content.rpg.trigger;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public record KillTrigger(ServerPlayer player, Entity target) implements QuestTrigger<KillTrigger> {
}
