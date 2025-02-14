package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class GLDimensionGen {

	public static final ResourceKey<DimensionType> DT_DREAM = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("dream"));
	public static final ResourceKey<LevelStem> LEVEL_DREAM = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("dream"));

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
		});
		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE).get(DT_DREAM).orElseThrow();
			var biome = ctx.lookup(Registries.BIOME).get(Biomes.THE_VOID).orElseThrow();
			ctx.register(LEVEL_DREAM, new LevelStem(dt, new FlatLevelSource(new FlatLevelGeneratorSettings(
					Optional.empty(), biome, List.of()
			))));
		});
	}

}
