package dev.xkmc.gensokyolegacy.content.dimension;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EmptyChunkGenerator extends ChunkGenerator {

	public static final MapCodec<EmptyChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Biome.CODEC.fieldOf("biome").forGetter(x -> x.biome)
	).apply(i, EmptyChunkGenerator::new));

	private final Holder<Biome> biome;

	public EmptyChunkGenerator(Holder<Biome> biome) {
		super(new FixedBiomeSource(biome));
		this.biome = biome;
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState randomState) {
		var state = Blocks.AIR.defaultBlockState();
		BlockState[] states = new BlockState[height.getHeight()];
		for (int i = 0; i < height.getHeight(); i++) {
			states[i] = state;
		}
		return new NoiseColumn(height.getMinBuildHeight(), states);
	}

	@Override
	public WeightedRandomList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> p_223134_, StructureManager p_223135_, MobCategory p_223136_, BlockPos p_223137_) {
		return WeightedRandomList.create();
	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel p_223087_, ChunkAccess p_223088_, StructureManager p_223089_) {

	}

	@Override
	public void applyCarvers(WorldGenRegion worldGenRegion, long l, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunkAccess, GenerationStep.Carving carving) {

	}

	@Override
	public int getGenDepth() {
		return 0;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunkAccess) {
		return CompletableFuture.completedFuture(chunkAccess);
	}

	@Override
	public int getBaseHeight(int i, int i1, Heightmap.Types types, LevelHeightAccessor levelHeightAccessor, RandomState randomState) {
		return 0;
	}

	@Override
	public void buildSurface(WorldGenRegion level, StructureManager structureManager, RandomState random, ChunkAccess chunk) {

	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion level) {

	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public void addDebugScreenInfo(List<String> info, RandomState random, BlockPos pos) {

	}

}
