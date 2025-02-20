package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.content.worldgen.feature.FloatingIslandData;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLWorldGen;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import javax.annotation.Nullable;
import java.util.List;

public class GLBiomeGen {

	public static final ResourceKey<Biome> BIOME_DREAM = biome("dream");

	public static final ResourceKey<Biome> BIOME_HOT = biome("dreamland_hot");
	public static final ResourceKey<Biome> BIOME_WARM_A = biome("dreamland_warm_a");
	public static final ResourceKey<Biome> BIOME_WARM_B = biome("dreamland_warm_b");
	public static final ResourceKey<Biome> BIOME_TEMP = biome("dreamland_temperate");
	public static final ResourceKey<Biome> BIOME_COOL_A = biome("dreamland_cool_a");
	public static final ResourceKey<Biome> BIOME_COOL_B = biome("dreamland_cool_b");
	public static final ResourceKey<Biome> BIOME_COLD = biome("dreamland_cold");
	public static final ResourceKey<Biome> BIOME_VOID = biome("dreamland_void");
	public static final ResourceKey<Biome> BIOME_ISLANDS = biome("dreamland_islands");
	public static final ResourceKey<Biome> BIOME_SKY = biome("dreamland_sky");
	public static final ResourceKey<Biome> BIOME_DEEP_VOID = biome("dreamland_deep_void");

	private static final ResourceKey<ConfiguredFeature<?, ?>> CF_ISLAND = configured("island");
	private static final ResourceKey<ConfiguredFeature<?, ?>> CF_PLATFORM = configured("platform");
	private static final ResourceKey<ConfiguredFeature<?, ?>> CF_CLOUD = configured("cloud");
	private static final ResourceKey<PlacedFeature> PF_ISLAND = place("island");
	private static final ResourceKey<PlacedFeature> PF_PLATFORM = place("platform");
	private static final ResourceKey<PlacedFeature> PF_CLOUD = place("cloud");

	public static void init(DataProviderInitializer init) {
		init.add(Registries.CONFIGURED_FEATURE, (ctx) -> {
			ctx.register(CF_ISLAND, new ConfiguredFeature<>(GLWorldGen.F_ISLAND.get(),
					new FloatingIslandData(2, 4, 1.5f, Blocks.STONE.defaultBlockState())));
			ctx.register(CF_PLATFORM, new ConfiguredFeature<>(GLWorldGen.F_ISLAND.get(),
					new FloatingIslandData(3, 5, 2f, Blocks.OBSIDIAN.defaultBlockState())));
			ctx.register(CF_CLOUD, new ConfiguredFeature<>(GLWorldGen.F_ISLAND.get(),
					new FloatingIslandData(2, 6, 1.2f, Blocks.GLASS.defaultBlockState())));
		});

		init.add(Registries.PLACED_FEATURE, (ctx) -> {
			var cf = ctx.lookup(Registries.CONFIGURED_FEATURE);
			ctx.register(PF_ISLAND, new PlacedFeature(cf.getOrThrow(CF_ISLAND), List.of(
					RarityFilter.onAverageOnceEvery(1),
					PlacementUtils.countExtra(1, 0.5F, 1),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(70)),
					BiomeFilter.biome()
			)));
			ctx.register(PF_PLATFORM, new PlacedFeature(cf.getOrThrow(CF_PLATFORM), List.of(
					RarityFilter.onAverageOnceEvery(8),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(4), VerticalAnchor.absolute(28)),
					BiomeFilter.biome()
			)));
			ctx.register(PF_CLOUD, new PlacedFeature(cf.getOrThrow(CF_CLOUD), List.of(
					RarityFilter.onAverageOnceEvery(32),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(80), VerticalAnchor.absolute(128)),
					BiomeFilter.biome()
			)));

		});


		init.add(Registries.BIOME, (ctx) -> {
			var pf = ctx.lookup(Registries.PLACED_FEATURE);
			var wc = ctx.lookup(Registries.CONFIGURED_CARVER);

			ctx.register(BIOME_DREAM, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_HOT, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_WARM_A, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_WARM_B, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_TEMP, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_COOL_A, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_COOL_B, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_COLD, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_VOID, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_ISLANDS, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc)
							.addFeature(GenerationStep.Decoration.RAW_GENERATION, pf.getOrThrow(PF_ISLAND)),
					Musics.END
			));

			ctx.register(BIOME_SKY, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc)
							.addFeature(GenerationStep.Decoration.RAW_GENERATION, pf.getOrThrow(PF_CLOUD)),
					Musics.END
			));

			ctx.register(BIOME_DEEP_VOID, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc)
							.addFeature(GenerationStep.Decoration.RAW_GENERATION, pf.getOrThrow(PF_PLATFORM)),
					Musics.END
			));

		});
	}

	private static ResourceKey<Biome> biome(String id) {
		return ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc(id));
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> configured(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, GensokyoLegacy.loc(id));
	}

	private static ResourceKey<PlacedFeature> place(String id) {
		return ResourceKey.create(Registries.PLACED_FEATURE, GensokyoLegacy.loc(id));
	}


	private static Biome biome(
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		return biome(false, 0.5f, 0.5f, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPercipitation, float temperature, float downfall,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		return biome(hasPercipitation, temperature, downfall, 4159204, 329011, null, null, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPrecipitation, float temperature, float downfall,
			int waterColor, int waterFogColor,
			@Nullable Integer grassCol, @Nullable Integer foliageCol,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		BiomeSpecialEffects.Builder biomespecialeffects$builder = new BiomeSpecialEffects.Builder()
				.waterColor(waterColor)
				.waterFogColor(waterFogColor)
				.fogColor(12638463)
				.skyColor(calculateSkyColor(temperature))
				.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
				.backgroundMusic(bgm);
		if (grassCol != null) {
			biomespecialeffects$builder.grassColorOverride(grassCol);
		}
		if (foliageCol != null) {
			biomespecialeffects$builder.foliageColorOverride(foliageCol);
		}
		return new Biome.BiomeBuilder()
				.hasPrecipitation(hasPrecipitation)
				.temperature(temperature)
				.downfall(downfall)
				.specialEffects(biomespecialeffects$builder.build())
				.mobSpawnSettings(spawns.build())
				.generationSettings(gen.build())
				.build();
	}

	protected static int calculateSkyColor(float temperature) {
		float f = Mth.clamp(temperature / 3.0F, -1.0F, 1.0F);
		return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
	}


}
