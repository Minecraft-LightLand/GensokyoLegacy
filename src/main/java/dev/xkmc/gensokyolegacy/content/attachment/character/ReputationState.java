package dev.xkmc.gensokyolegacy.content.attachment.character;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.TargetKind;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public enum ReputationState {
	FRIEND, STRANGER, JERK, ENEMY;

	public static Component toInfo(int reputation) {
		return GLLang.INFO$ENTITY_REPUTATION.get(reputation).withStyle(
				switch (CharacterData.getState(reputation)) {
					case FRIEND -> ChatFormatting.GREEN;
					case STRANGER -> ChatFormatting.WHITE;
					case JERK -> ChatFormatting.YELLOW;
					case ENEMY -> ChatFormatting.RED;
				}
		);
	}

	public TargetKind asTargetKind() {
		return switch (this) {
			case FRIEND -> TargetKind.WORTHY;
			case STRANGER -> TargetKind.NONE;
			default -> TargetKind.ENEMY;
		};
	}

}
