package dev.xkmc.gensokyolegacy.init.data.dimension;

import com.tterrag.registrate.providers.DataProviderInitializer;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf.GateType;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf.LeafType;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf.RoomLeafManager;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.FrameConfig;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.MazeChunkGenerator;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.SimpleRoomContent;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.FixedBiomeSource;
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

	public static final ResourceKey<DimensionType> DT_MIST = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("mist"));
	public static final ResourceKey<NoiseGeneratorSettings> NGS_MIST = ResourceKey.create(Registries.NOISE_SETTINGS, GensokyoLegacy.loc("mist"));
	public static final ResourceKey<LevelStem> LEVEL_MIST = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("mist"));

	public static final ResourceKey<DimensionType> DT_MAZE = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("maze"));
	public static final ResourceKey<LevelStem> LEVEL_MAZE = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("maze"));

	public static final ResourceKey<DimensionType> DT_DREAMLAND = ResourceKey.create(Registries.DIMENSION_TYPE, GensokyoLegacy.loc("dreamland"));
	public static final ResourceKey<NormalNoise.NoiseParameters> NP_SIMPLE = ResourceKey.create(Registries.NOISE, GensokyoLegacy.loc("simple"));
	public static final ResourceKey<NoiseGeneratorSettings> NGS_DREAMLAND = ResourceKey.create(Registries.NOISE_SETTINGS, GensokyoLegacy.loc("dreamland"));
	public static final ResourceKey<LevelStem> LEVEL_DREAMLAND = ResourceKey.create(Registries.LEVEL_STEM, GensokyoLegacy.loc("dreamland"));

	public static void init(DataProviderInitializer init) {
		float threshold = 0.4f;
		float center = threshold + 0.25f;
		var biomeSet = new ClimateBuilder(
				ParamDiv.trinary(0.5f),
				ParamDiv.positive(threshold - 0.05f),
				ParamDiv.trinary(threshold - 0.05f),
				ParamDiv.polar()
		);
		{
			var root = biomeSet.start()
					.startRule(SurfaceRules.ON_FLOOR)
					.addRule(SurfaceRules.state(Blocks.DIRT.defaultBlockState()));

			var low = root.depth(e -> e.not(1));
			var land = low.cont(e -> e.get(1));

			var top = root
					.cont(e -> e.tip(center + 0.05f))
					.depth(e -> ParamDiv.span(-0.2f, 0.45f));

			top.temp(e -> e.tip(center)).biome(GLBiomeGen.BIOME_HOT, 0f)
					.addRule(SurfaceRules.state(Blocks.GOLD_BLOCK.defaultBlockState()));

			land.temp(e -> e.get(1))
					.vege(e -> e.get(1)).biome(GLBiomeGen.BIOME_WARM_A, 0.1f)
					.addRule(SurfaceRules.state(Blocks.BLACKSTONE.defaultBlockState())).end().end()
					.vege(e -> e.get(-1)).biome(GLBiomeGen.BIOME_WARM_B, 0.1f)
					.addRule(SurfaceRules.state(Blocks.NETHERRACK.defaultBlockState()));

			land.temp(e -> e.get(0)).biome(GLBiomeGen.BIOME_TEMP, 0.1f);

			land.temp(e -> e.get(-1))
					.vege(e -> e.get(1)).biome(GLBiomeGen.BIOME_COOL_A, 0.1f)
					.addRule(SurfaceRules.state(Blocks.SNOW_BLOCK.defaultBlockState())).end().end()
					.vege(e -> e.get(-1)).biome(GLBiomeGen.BIOME_COOL_B, 0.1f)
					.addRule(SurfaceRules.state(Blocks.PACKED_ICE.defaultBlockState()));

			top.temp(e -> e.tip(-center)).biome(GLBiomeGen.BIOME_COLD, 0f)
					.addRule(SurfaceRules.state(Blocks.BLUE_ICE.defaultBlockState()));

			low.cont(e -> e.get(0)).biome(GLBiomeGen.BIOME_VOID, 0.1f);
			low.cont(e -> e.tip(0)).biome(GLBiomeGen.BIOME_ISLANDS, 0.05f);
			low.cont(e -> e.tip(0)).depth(e -> e.tip(-0.9f)).biome(GLBiomeGen.BIOME_DEEP_VOID, 0f);

			root.depth(e -> e.get(1)).biome(GLBiomeGen.BIOME_SKY, 0.05f);

		}

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
			ctx.register(DT_MAZE, new DimensionType(
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
			ctx.register(DT_MIST, new DimensionType(
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
			ctx.register(NGS_DREAMLAND, NoiseBuilder.islands(ctx, new NoiseBuilder.SlideData(
					0, 64,
					0, 512, -1500 / 64d,
					4, 32, -15 / 64d,
					0.64, -0.4, 0.5,
					NP_SIMPLE
			), biomeSet.buildRules()));

			ctx.register(NGS_MIST, NoiseBuilder.mist(ctx, new NoiseBuilder.SlideData(
					0, 128,
					72, 184, -30 / 64d,
					4, 32, -15 / 64d,
					0.64, -0.2, 0.5,
					NP_SIMPLE
			), SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState()))));
		});

		init.add(Registries.LEVEL_STEM, (ctx) -> {
			var dt = ctx.lookup(Registries.DIMENSION_TYPE);
			var biome = ctx.lookup(Registries.BIOME);
			var noise = ctx.lookup(Registries.NOISE_SETTINGS);

			ctx.register(LEVEL_DREAM, new LevelStem(dt.getOrThrow(DT_DREAM),
					new FlatLevelSource(new FlatLevelGeneratorSettings(Optional.empty(),
							biome.getOrThrow(GLBiomeGen.BIOME_DREAM), List.of()))));

			ctx.register(LEVEL_DREAMLAND, new LevelStem(dt.getOrThrow(DT_DREAMLAND), new NoiseBasedChunkGenerator(
					MultiNoiseBiomeSource.createFromList(biomeSet.climate(biome)), noise.getOrThrow(NGS_DREAMLAND))));

			ctx.register(LEVEL_MIST, new LevelStem(dt.getOrThrow(DT_MIST), new NoiseBasedChunkGenerator(
					new FixedBiomeSource(biome.getOrThrow(GLBiomeGen.BIOME_DREAM)),
					noise.getOrThrow(NGS_MIST))));

			{
				var blocks = new FrameConfig(
						Blocks.OBSIDIAN.defaultBlockState(),
						Blocks.STONE.defaultBlockState()
				);
				var leaf = new RoomLeafManager.Builder().addCell(
						new SimpleRoomContent(Blocks.MOSS_CARPET.defaultBlockState(), Blocks.SHROOMLIGHT.defaultBlockState()),
						new LeafType(GateType.SIDE, 0), 100
				).addCell(
						new SimpleRoomContent(Blocks.PRISMARINE_BRICK_SLAB.defaultBlockState(), Blocks.SEA_LANTERN.defaultBlockState()),
						new LeafType(GateType.SIDE, 1), 100
				).addCell(
						new SimpleRoomContent(Blocks.AIR.defaultBlockState(), Blocks.SEA_LANTERN.defaultBlockState()),
						new LeafType(GateType.DOWN, 0), 100
				).addCell(
						new SimpleRoomContent(Blocks.AIR.defaultBlockState(), Blocks.SEA_LANTERN.defaultBlockState()),
						new LeafType(GateType.DOWN, 1), 100
				).addCell(
						new SimpleRoomContent(Blocks.SLIME_BLOCK.defaultBlockState(), Blocks.AIR.defaultBlockState()),
						new LeafType(GateType.UP, 0), 100
				).addCell(
						new SimpleRoomContent(Blocks.SLIME_BLOCK.defaultBlockState(), Blocks.AIR.defaultBlockState()),
						new LeafType(GateType.UP, 1), 100
				).build();
				ctx.register(LEVEL_MAZE, new LevelStem(dt.getOrThrow(DT_MAZE),
						new MazeChunkGenerator(biome.getOrThrow(GLBiomeGen.BIOME_DREAM),
								blocks, leaf)));
			}
		});

	}

}
