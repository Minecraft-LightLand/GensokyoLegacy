package dev.xkmc.gensokyolegacy.content.entity.module;

import dev.xkmc.gensokyolegacy.content.entity.behavior.combat.TargetKind;

public enum ReputationState {
	FRIEND, STRANGER, JERK, ENEMY;


	public TargetKind asTargetKind() {
		return switch (this) {
			case FRIEND -> TargetKind.WORTHY;
			case STRANGER -> TargetKind.NONE;
			default -> TargetKind.ENEMY;
		};
	}
}
