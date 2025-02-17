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

	public static final ResourceKey<Biome> BIOME_DREAMLAND = ResourceKey.create(Registries.BIOME, GensokyoLegacy.loc("dreamland"));

	public static void init(DataProviderInitializer init) {
		init.add(Registries.BIOME, (ctx) -> {
			var pf = ctx.lookup(Registries.PLACED_FEATURE);
			var wc = ctx.lookup(Registries.CONFIGURED_CARVER);

			ctx.register(BIOME_DREAM, biome(
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.Builder(pf, wc),
					Musics.END
			));

			ctx.register(BIOME_DREAMLAND, biome(
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
