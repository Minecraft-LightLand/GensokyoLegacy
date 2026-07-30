package dev.xkmc.gensokyolegacy.content.rpg.core;

import com.mojang.serialization.MapCodec;

public interface CodecElement<T> {

	MapCodec<T> codec();

}
