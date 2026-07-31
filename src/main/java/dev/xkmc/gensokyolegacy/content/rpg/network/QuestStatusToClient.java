package dev.xkmc.gensokyolegacy.content.rpg.network;

import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.quest.QuestData;
import dev.xkmc.gensokyolegacy.content.ui.quest.QuestOverlay;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public record QuestStatusToClient(
		ResourceLocation id, QuestData data, Reason reason
) implements SerialPacketBase<QuestStatusToClient> {

	public enum Reason {
		START, COMPLETE, UPDATE
	}

	@Override
	public void handle(Player player) {
		GLMeta.QUEST.type().getOrCreate(player).replace(id, data);
		if (reason == Reason.START) {
			player.playSound(SoundEvents.PLAYER_LEVELUP);
		} else if (reason == Reason.COMPLETE) {
			player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE);
		} else if (reason == Reason.UPDATE) {
			ClientHandler.setQuest(id);
		}
	}

	public static class ClientHandler {

		public static void setQuest(ResourceLocation id) {
			var level = Minecraft.getInstance().level;
			if (level == null) return;
			var quest = level.registryAccess().holder(ResourceKey.create(CodecRegistry.Keys.QUEST, id));
			if (quest.isEmpty()) return;
			QuestOverlay.setQuest(quest.get(), level.getGameTime());
		}

	}

}
