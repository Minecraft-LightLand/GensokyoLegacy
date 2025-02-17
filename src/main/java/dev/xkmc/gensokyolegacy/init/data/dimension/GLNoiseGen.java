package dev.xkmc.gensokyolegacy.init.data.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;

import java.util.List;

public class GLNoiseGen {

	private static final ResourceKey<DensityFunction> SHIFT_X = mcKey("shift_x");
	private static final ResourceKey<DensityFunction> SHIFT_Z = mcKey("shift_z");

	public static NoiseGeneratorSettings islands(BootstrapContext<NoiseGeneratorSettings> ctx, SlideData data, SurfaceRules.RuleSource surface) {
		var zero = DensityFunctions.constant(0);

		var simple_a = ctx.lookup(Registries.NOISE).getOrThrow(GLDimensionGen.NP_SIMPLE_A);
		var simple_b = ctx.lookup(Registries.NOISE).getOrThrow(GLDimensionGen.NP_SIMPLE_B);
		var simple_0 = ctx.lookup(Registries.NOISE).getOrThrow(GLDimensionGen.NP_SIMPLE_0);
		var terrain_a = DensityFunctions.mappedNoise(simple_a, -1, 1);
		var terrain_b = DensityFunctions.mappedNoise(simple_b, 1, -1);
		var terrain_0 = DensityFunctions.mappedNoise(simple_0, -1, 1);
		var terrain_noise = DensityFunctions.mul(terrain_0, DensityFunctions.constant(data.noise));
		var terrain_main = DensityFunctions.mul(terrain_a, terrain_b).abs();
		var terrain = DensityFunctions.add(DensityFunctions.constant(data.sparse),
				new AddNoise(terrain_main, terrain_noise, data.slope));
		var shiftedDepth = DensityFunctions.yClampedGradient(data.maxY, data.lowBody, 1, 0);
		var shiftedTemp = DensityFunctions.noise(simple_a);
		var shiftedVege = DensityFunctions.noise(simple_b);

		var router = new NoiseRouter(
				zero, // barrier
				zero, // fluid flood
				zero, // fluid spread
				zero, // lava
				shiftedTemp, // temperature
				shiftedVege, // vegetation
				zero, // continents
				zero, // erosion
				shiftedDepth, // depth
				zero, // ridges
				zero, // initial
				data.slide(ctx, terrain), // final
				zero, // vein toggle
				zero, // vein ridged
				zero // vein gap
		);

		return new NoiseGeneratorSettings(
				new NoiseSettings(0, 256, 2, 1),
				Blocks.STONE.defaultBlockState(), Blocks.AIR.defaultBlockState(),
				router, surface,
				List.of(), -64, false,
				false, false, true
		);
	}

	private static ResourceKey<DensityFunction> mcKey(String location) {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.withDefaultNamespace(location));
	}

	public record SlideData(
			int minY, int maxY, int hill, int margin,
			float lerp0, int bottom, int lowBody, float lerp1,
			float post, float sparse, float noise, float slope
	) {

		public DensityFunction slide(BootstrapContext<NoiseGeneratorSettings> ctx, DensityFunction cont) {

			var topSlope = DensityFunctions.yClampedGradient(
					minY + maxY - hill, minY + maxY + margin, 1, 0);
			var bottomSlope = DensityFunctions.yClampedGradient(
					minY + bottom, minY + lowBody, 0, 1);

			var topSmoothed = DensityFunctions.lerp(topSlope, lerp0, cont);
			var bottomSmoothed = DensityFunctions.lerp(bottomSlope, lerp1, topSmoothed);
			return DensityFunctions.mul(
					DensityFunctions.interpolated(DensityFunctions.blendDensity(bottomSmoothed)),
					DensityFunctions.constant(post)
			).squeeze();
		}

	}

}
