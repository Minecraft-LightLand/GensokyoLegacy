package dev.xkmc.gensokyolegacy.content.quest.core;

import com.mojang.serialization.MapCodec;

public interface QuestElement<T> {

	MapCodec<T> codec();

}
