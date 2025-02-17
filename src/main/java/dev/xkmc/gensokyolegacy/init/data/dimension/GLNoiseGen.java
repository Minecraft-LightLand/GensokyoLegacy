package dev.xkmc.gensokyolegacy.init.data.dimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

public class GLNoiseGen {

	private static final ResourceKey<DensityFunction> SHIFT_X = mcKey("shift_x");
	private static final ResourceKey<DensityFunction> SHIFT_Z = mcKey("shift_z");

	public static NoiseGeneratorSettings islands(BootstrapContext<NoiseGeneratorSettings> ctx, SlideData data, SurfaceRules.RuleSource surface) {
		var params = ctx.lookup(Registries.NOISE);
		var densities = ctx.lookup(Registries.DENSITY_FUNCTION);

		var zero = DensityFunctions.constant(0);
		var shift_x = new DensityFunctions.HolderHolder(densities.getOrThrow(SHIFT_X));
		var shift_z = new DensityFunctions.HolderHolder(densities.getOrThrow(SHIFT_Z));

		var cont = ctx.lookup(Registries.NOISE).getOrThrow(data.param);
		var terrain = DensityFunctions.mappedNoise(cont, -1 + data.sparse, 1 + data.sparse);

		var shiftedTemp = DensityFunctions.shiftedNoise2d(shift_x, shift_z, 0.25, params.getOrThrow(Noises.TEMPERATURE));
		var shiftedVege = DensityFunctions.shiftedNoise2d(shift_x, shift_z, 0.25, params.getOrThrow(Noises.VEGETATION));
		var shiftedCont = DensityFunctions.interpolated(DensityFunctions.flatCache(DensityFunctions.max(
				new DensityFunctions.ShiftedNoise(
						DensityFunctions.zero(),
						DensityFunctions.constant(data.minY + data.maxY - data.hill - 4),
						DensityFunctions.zero(),
						1, 0, new DensityFunction.NoiseHolder(cont)),
				new DensityFunctions.ShiftedNoise(
						DensityFunctions.zero(),
						DensityFunctions.constant(data.minY + data.maxY - data.hill - 24),
						DensityFunctions.zero(),
						1, 0, new DensityFunction.NoiseHolder(cont))
		)));
		var router = new NoiseRouter(
				zero, // barrier
				zero, // fluid flood
				zero, // fluid spread
				zero, // lava
				shiftedTemp, // temperature
				shiftedVege, // vegetation
				shiftedCont, // continents
				zero, // erosion
				zero, // depth
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
			double lerp0, int bottom, int lowBody, double lerp1,
			double post, double sparse,
			ResourceKey<NormalNoise.NoiseParameters> param
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
