package dev.xkmc.gensokyolegacy.content.dimension;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

import javax.annotation.Nullable;
import java.util.OptionalLong;

public class GLDimensionGen {

	public static final ResourceKey<Biome> BIOME_GAP = ResourceKey.create(Registries.BIOME, loc("gap"));
	public static final ResourceKey<DimensionType> DT_GAP = ResourceKey.create(Registries.DIMENSION_TYPE, loc("gap"));
	public static final ResourceKey<LevelStem> LEVEL_GAP = ResourceKey.create(Registries.LEVEL_STEM, loc("gap"));
	public static final ResourceKey<Level> GAP = Registries.levelStemToLevel(LEVEL_GAP);

	public static void init(DataProviderInitializer init) {

		init.add(Registries.BIOME, (ctx) -> {
			ctx.register(BIOME_GAP, biome(6840176,
					new MobSpawnSettings.Builder(),
					new BiomeGenerationSettings.PlainBuilder(),
					Musics.createGameMusic(SoundEvents.MUSIC_END)
			));
		});

		init.add(Registries.DIMENSION_TYPE, (ctx) -> {
			var spawn = new DimensionType.MonsterSettings(true, false,
					UniformInt.of(0, 7), 0);
			ctx.register(DT_GAP, new DimensionType(
					OptionalLong.of(18000L),
					false, false, false, false,
					1, true, false,
					0, 256, 256,
					BlockTags.INFINIBURN_OVERWORLD,
					BuiltinDimensionTypes.END_EFFECTS, 1f, spawn
			));
		});

		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE);
			var biome = ctx.lookup(Registries.BIOME);
			ctx.register(LEVEL_GAP, new LevelStem(dt.getOrThrow(DT_GAP), new EmptyChunkGenerator(biome.getOrThrow(BIOME_GAP))));
		});

	}

	private static ResourceLocation loc(String id) {
		return GensokyoLegacy.loc(id);
	}

	private static Biome biome(int fogColor,
	                           MobSpawnSettings.Builder spawns,
	                           BiomeGenerationSettings.PlainBuilder gen,
	                           @Nullable Music bgm
	) {
		return biome(false, 2, 0, fogColor, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPercipitation, float temperature, float downfall, int fogColor,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		return biome(hasPercipitation, temperature, downfall, 4159204, 329011, fogColor, null, null, spawns, gen, bgm);
	}

	private static Biome biome(
			boolean hasPrecipitation, float temperature, float downfall,
			int waterColor, int waterFogColor, int fogColor,
			@Nullable Integer grassCol, @Nullable Integer foliageCol,
			MobSpawnSettings.Builder spawns,
			BiomeGenerationSettings.PlainBuilder gen,
			@Nullable Music bgm
	) {
		BiomeSpecialEffects.Builder biomespecialeffects$builder = new BiomeSpecialEffects.Builder()
				.waterColor(waterColor)
				.waterFogColor(waterFogColor)
				.fogColor(fogColor)
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
