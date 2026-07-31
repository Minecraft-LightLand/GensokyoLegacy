package dev.xkmc.gensokyolegacy.content.ui.quest;

import dev.xkmc.gensokyolegacy.content.rpg.quest.Quest;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import dev.xkmc.l2itemselector.overlay.InfoSideBar;
import dev.xkmc.l2itemselector.overlay.SideBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuestOverlay extends InfoSideBar<QuestOverlay.QuestSignature> {

	private static @Nullable Holder<Quest> quest;
	private static long timestamp;

	public static void setQuest(Holder<Quest> q, long t) {
		quest = q;
		timestamp = t;
	}

	public QuestOverlay() {
		super(40, 10);
	}

	public record QuestSignature(@Nullable Holder<Quest> quest, long timestamp) implements Signature<QuestSignature> {

		@Override
		public boolean shouldRefreshIdle(SideBar<?> sideBar, @Nullable QuestOverlay.QuestSignature old) {
			return !this.equals(old);
		}

	}

	@Override
	public QuestSignature getSignature() {
		return new QuestSignature(quest, timestamp);
	}

	@Override
	public boolean isScreenOn() {
		if (Minecraft.getInstance().screen != null) return false;
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || quest == null) return false;
		var current = player.level().getGameTime();
		var data = GLMeta.QUEST.type().getOrCreate(player).getData(quest.unwrapKey().orElseThrow().location());
		if (!data.started) return false;
		return quest != null && current > timestamp && current < timestamp + 60;
	}

	@Override
	protected List<Component> getText() {
		LocalPlayer player = Minecraft.getInstance().player;
		if (player == null || quest == null) return List.of();
		var data = GLMeta.QUEST.type().getOrCreate(player).getData(quest.unwrapKey().orElseThrow().location());
		if (!data.started) return List.of();
		var info = new QuestInfo(quest.value(), data);
		return info.getBody(player);
	}

}
