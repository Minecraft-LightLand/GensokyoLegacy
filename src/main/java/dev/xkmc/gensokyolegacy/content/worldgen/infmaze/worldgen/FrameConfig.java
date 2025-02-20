package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public record FrameConfig(BlockState hard, BlockState wall) {

	public static final Codec<FrameConfig> CODEC = RecordCodecBuilder.create(i -> i.group(
			BlockState.CODEC.fieldOf("skeleton").forGetter(FrameConfig::hard),
			BlockState.CODEC.fieldOf("filler").forGetter(FrameConfig::wall)
	).apply(i, FrameConfig::new));

}
