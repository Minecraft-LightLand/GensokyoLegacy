package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class GLDimensionGen {


	public static final ResourceKey<DimensionType> DT_DREAM = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("dream"));
	public static final ResourceKey<LevelStem> LEVEL_DREAM = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("dream"));

	public static final ResourceKey<DimensionType> DT_DREAMLAND = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("dreamland"));
	public static final ResourceKey<NormalNoise.NoiseParameters> NP_SIMPLE = ResourceKey.create(Registries.NOISE, GensokyoLegacy.loc("simple"));
	public static final ResourceKey<NoiseGeneratorSettings> NGS_DREAMLAND = ResourceKey.create(Registries.NOISE_SETTINGS, GensokyoLegacy.loc("dreamland"));
	public static final ResourceKey<LevelStem> LEVEL_DREAMLAND = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("dreamland"));

	public static void init(DataProviderInitializer init) {

		init.add(Registries.DIMENSION_TYPE, (ctx) -> {
			var spawn = new DimensionType.MonsterSettings(true, false,
					UniformInt.of(0, 7), 0);
			ctx.register(DT_DREAM, new DimensionType(
					OptionalLong.of(18000L),
					false, false, false, false,
					1, false, false,
					0, 256, 256,
					BlockTags.INFINIBURN_OVERWORLD,
					BuiltinDimensionTypes.END_EFFECTS, 0, spawn
			));
			ctx.register(DT_DREAMLAND, new DimensionType(
					OptionalLong.of(18000L),
					false, false, false, false,
					1, false, false,
					0, 256, 256,
					BlockTags.INFINIBURN_OVERWORLD,
					BuiltinDimensionTypes.END_EFFECTS, 0, spawn
			));
		});

		init.add(Registries.NOISE, (ctx) -> {
			ctx.register(NP_SIMPLE, new NormalNoise.NoiseParameters(-6, DoubleList.of(1, 0.7, 0.6, 0.5)));
		});

		init.add(Registries.NOISE_SETTINGS, (ctx) -> {
			var data = new GLNoiseGen.SlideData(
					0, 64,
					0, 512, -1500 / 64d,
					4, 32, -15 / 64d,
					0.64, -0.4,
					NP_SIMPLE
			);
			var surface = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
					SurfaceRules.sequence(
							SurfaceRules.ifTrue(SurfaceRules.isBiome(GLBiomeGen.BIOME_MAINLAND),
									SurfaceRules.state(Blocks.GOLD_BLOCK.defaultBlockState())),
							SurfaceRules.ifTrue(SurfaceRules.isBiome(GLBiomeGen.BIOME_EDGE),
									SurfaceRules.state(Blocks.IRON_BLOCK.defaultBlockState())),
							SurfaceRules.ifTrue(SurfaceRules.isBiome(GLBiomeGen.BIOME_ISLAND),
									SurfaceRules.state(Blocks.DIRT.defaultBlockState())),
							SurfaceRules.ifTrue(SurfaceRules.isBiome(GLBiomeGen.BIOME_VOID),
									SurfaceRules.state(Blocks.DIAMOND_BLOCK.defaultBlockState()))
					)
			);
			ctx.register(NGS_DREAMLAND, GLNoiseGen.islands(ctx, data, surface));
		});

		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE);
			var biome = ctx.lookup(Registries.BIOME);
			var noise = ctx.lookup(Registries.NOISE_SETTINGS);
			ctx.register(LEVEL_DREAM, new LevelStem(dt.getOrThrow(DT_DREAM),
					new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(),
							biome.getOrThrow(GLBiomeGen.BIOME_DREAM), List.of()))));

			var climate = new Climate.ParameterList<Holder<Biome>>(List.of(
					Pair.of(point(0.9f, 1f, 0.6f, 1f, 0f), biome.get(GLBiomeGen.BIOME_MAINLAND).orElseThrow()),
					Pair.of(point(0f, 1f, 0.4f, 1f, 0.1f), biome.get(GLBiomeGen.BIOME_EDGE).orElseThrow()),
					Pair.of(point(0f, 1f, 0.1f, 0.3f, 0.1f), biome.get(GLBiomeGen.BIOME_ISLAND).orElseThrow()),
					Pair.of(point(0f, 1f, -1f, 0f, 0.1f), biome.get(GLBiomeGen.BIOME_VOID).orElseThrow())
			));
			ctx.register(LEVEL_DREAMLAND, new LevelStem(dt.getOrThrow(DT_DREAMLAND), new NoiseBasedChunkGenerator(
					MultiNoiseBiomeSource.createFromList(climate), noise.getOrThrow(NGS_DREAMLAND))));
		});

	}

	private static Climate.ParameterPoint point(float d0, float d1, float c0, float c1, float offset) {
		return Climate.parameters(
				Climate.Parameter.point(0),
				Climate.Parameter.point(0),
				Climate.Parameter.span(c0, c1),
				Climate.Parameter.point(0),
				Climate.Parameter.span(d0, d1),
				Climate.Parameter.point(0),
				offset
		);
	}


}
