package dev.xkmc.gensokyolegacy.content.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record FloatingIslandData(int minRad, int maxRad, float slope, BlockState state) implements FeatureConfiguration {

	public static final Codec<FloatingIslandData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("minRad").forGetter(FloatingIslandData::minRad),
			Codec.INT.fieldOf("maxRad").forGetter(FloatingIslandData::maxRad),
			Codec.FLOAT.fieldOf("slope").forGetter(FloatingIslandData::slope),
			BlockState.CODEC.fieldOf("state").forGetter(FloatingIslandData::state)
	).apply(i, FloatingIslandData::new));

}
