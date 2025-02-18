package dev.xkmc.gensokyolegacy.content.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record EndIslandData(int minRad, int maxRad, float slope, BlockState state) implements FeatureConfiguration {

	public static final Codec<EndIslandData> CODEC = RecordCodecBuilder.create(i -> i.group(
			Codec.INT.fieldOf("minRad").forGetter(EndIslandData::minRad),
			Codec.INT.fieldOf("maxRad").forGetter(EndIslandData::maxRad),
			Codec.FLOAT.fieldOf("slope").forGetter(EndIslandData::slope),
			BlockState.CODEC.fieldOf("state").forGetter(EndIslandData::state)
	).apply(i, EndIslandData::new));

}
