package dev.xkmc.gensokyolegacy.content.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class FloatingIslandFeature extends Feature<FloatingIslandData> {

	public FloatingIslandFeature(Codec<FloatingIslandData> codec) {
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<FloatingIslandData> ctx) {
		WorldGenLevel worldgenlevel = ctx.level();
		RandomSource rand = ctx.random();
		BlockPos blockpos = ctx.origin();
		var data = ctx.config();
		float rad = rand.nextFloat() * (data.maxRad() - data.minRad()) + data.minRad();

		for (int i = 0; rad > 0.5F; i--) {
			float radSq = (rad + 1) * (rad + 1);
			for (int j = Mth.floor(-rad); j <= Mth.ceil(rad); j++) {
				for (int k = Mth.floor(-rad); k <= Mth.ceil(rad); k++) {
					if (j * j + k * k <= radSq) {
						this.setBlock(worldgenlevel, blockpos.offset(j, i, k), data.state());
					}
				}
			}
			rad -= data.slope();
		}

		return true;
	}
}
