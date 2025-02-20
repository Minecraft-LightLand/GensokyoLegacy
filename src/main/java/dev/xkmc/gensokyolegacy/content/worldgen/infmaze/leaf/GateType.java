package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf;

import com.mojang.serialization.Codec;

public enum GateType {
	UP, DOWN, SIDE;

	public static final Codec<GateType> CODEC = Codec.STRING.xmap(e -> Enum.valueOf(GateType.class, e), Enum::name);

}
