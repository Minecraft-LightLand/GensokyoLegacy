package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.worldgen.common.EmptyChunkGenerator;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.GenerationConfig;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.InfiniMaze;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.LeafManager;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.leaf.RoomLeafManager;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2serial.util.LazyFunction;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;

public class MazeChunkGenerator extends EmptyChunkGenerator {

	private static final int CELL_WIDTH = 8, SCALE = 5, HEIGHT = CELL_WIDTH << SCALE;
	private static final ResourceLocation RL = GensokyoLegacy.loc("maze_chunkgen");
	private static final LeafManager MANAGER = new RoomLeafManager();

	private static final FrameConfig BLOCKS = new FrameConfig(
			Blocks.AIR.defaultBlockState(),
			Blocks.BEDROCK.defaultBlockState(),
			Blocks.OBSIDIAN.defaultBlockState(),
			Blocks.STONE.defaultBlockState()
	);

	public static final MapCodec<MazeChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Biome.CODEC.fieldOf("biome").forGetter(x -> x.biome)
	).apply(i, MazeChunkGenerator::new));

	private final Holder<Biome> biome;
	private final LazyFunction<Long, InfiniMaze> maze;
	private final ChunkFiller filler;

	public MazeChunkGenerator(Holder<Biome> biome) {
		super(new FixedBiomeSource(biome));
		this.biome = biome;
		maze = LazyFunction.create(rand -> new InfiniMaze(new GenerationConfig(SCALE, rand, MANAGER)));
		filler = new ChunkFiller(CELL_WIDTH, SCALE, BLOCKS);
	}

	@Override
	public int getGenDepth() {
		return HEIGHT;
	}

	@Override
	protected MapCodec<MazeChunkGenerator> codec() {
		return CODEC;
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState random, StructureManager structures, ChunkAccess access) {
		return CompletableFuture.supplyAsync(() -> {
			InfiniMaze maze = this.maze.get(random.getOrCreateRandomFactory(RL).at(0, 0, 0).nextLong());
			ChunkPos pos = access.getPos();
			filler.fillChunk(maze, pos, access, random);
			return access;
		}, Util.backgroundExecutor());
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor height, RandomState random) {
		return getGenDepth();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		BlockState[] states = new BlockState[height.getHeight()];
		for (int i = 0; i < height.getHeight(); i++) {
			states[i] = BLOCKS.wall();
		}
		return new NoiseColumn(height.getMinBuildHeight(), states);
	}

}
