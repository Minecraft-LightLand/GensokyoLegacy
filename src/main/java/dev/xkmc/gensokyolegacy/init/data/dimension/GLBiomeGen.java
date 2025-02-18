package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.*;

import javax.annotation.Nullable;

public class GLBiomeGen {

	public static final ResourceKey<Biome> BIOME_DREAM = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dream"));

	public static final ResourceKey<Biome> BIOME_HOT = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_hot"));
	public static final ResourceKey<Biome> BIOME_WARM_A = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_warm_a"));
	public static final ResourceKey<Biome> BIOME_WARM_B = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_warm_b"));
	public static final ResourceKey<Biome> BIOME_TEMP = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_temperate"));
	public static final ResourceKey<Biome> BIOME_COOL_A = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_cool_a"));
	public static final ResourceKey<Biome> BIOME_COOL_B = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_cool_b"));
	public static final ResourceKey<Biome> BIOME_COLD = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_cold"));
	public static final ResourceKey<Biome> BIOME_VOID = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_void"));
	public static final ResourceKey<Biome> BIOME_DEEP_VOID = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland_deep_void"));

	public static void init(DataProviderInitializer init) {
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

			ctx.register(BIOME_DEEP_VOID, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

		});
	}

	private static Biome biome(
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.Builder gen,
			@Nullable Music bgm
	) {
		return biome(false, 0.5f, 0.5f, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPercipitation, float temperature, float downfall,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.Builder gen,
			@Nullable Music bgm
	) {
		return biome(hasPercipitation, temperature, downfall, 4159204, 329011, null, null, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPrecipitation, float temperature, float downfall,
			int waterColor, int waterFogColor,
			@Nullable Integer grassCol, @Nullable Integer foliageCol,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.Builder gen,
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
