package dev.xkmc.gensokyolegacy.content.client.debug;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterData;
import dev.xkmc.gensokyolegacy.content.attachment.index.StructureKey;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.l2serial.network.SerialPacketBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public record CharacterInfoToClient(
		ArrayList<Component> info,
		ArrayList<Component> advanced
) implements SerialPacketBase<CharacterInfoToClient> {

	public static CharacterInfoToClient ofEntity(
			@Nullable StructureKey home,
			@Nullable BlockPos bed,
			int reputation,
			int feedCD,
			String activity
	) {
		ArrayList<Component> info = new ArrayList<>();
		ArrayList<Component> advanced = new ArrayList<>();
		if (home == null || bed == null) {
			info.add(GLLang.INFO_ENTITY_UNBOUND.get().withStyle(ChatFormatting.GRAY));
		} else {
			var p = bed;
			info.add(GLLang.INFO_ENTITY_BED.get(p.getX(), p.getY(), p.getZ()).withStyle(ChatFormatting.GRAY));
		}
		info.add(GLLang.INFO_ENTITY_REPUTATION.get(reputation).withStyle(
				switch (CharacterData.getState(reputation)) {
					case FRIEND -> ChatFormatting.GREEN;
					case STRANGER -> ChatFormatting.WHITE;
					case JERK -> ChatFormatting.YELLOW;
					case ENEMY -> ChatFormatting.RED;
				}
		));
		if (feedCD > 0) {
			info.add(GLLang.INFO_ENTITY_FEED.time(feedCD).withStyle(ChatFormatting.GRAY));
		}
		if (!activity.isEmpty()) {
			String[] strs = activity.split("\n");
			for (var e : strs) {
				advanced.add(Component.literal(e).withStyle(ChatFormatting.DARK_GRAY));
			}
		}
		return new CharacterInfoToClient(info, advanced);
	}

	@Override
	public void handle(Player player) {
		InfoUpdateClientManager.handleCharacterInfo(this);
	}

}
