package dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.gensokyolegacy.content.worldgen.common.EmptyChunkGenerator;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.GenerationConfig;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.InfiniMaze;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.leaf.RoomLeafManager;
import dev.xkmc.l2serial.util.LazyFunction;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.concurrent.CompletableFuture;

public class MazeChunkGenerator extends EmptyChunkGenerator {

	private static final int CELL_WIDTH = 8, SCALE = 5, HEIGHT = CELL_WIDTH << SCALE;

	public static final MapCodec<MazeChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Biome.CODEC.fieldOf("biome").forGetter(x -> x.biome),
			FrameConfig.CODEC.fieldOf("blocks").forGetter(x -> x.blocks),
			RoomLeafManager.CODEC.fieldOf("rooms").forGetter(x -> x.leaf)
	).apply(i, MazeChunkGenerator::new));

	private final Holder<Biome> biome;
	private final LazyFunction<Long, InfiniMaze> maze;
	private final ChunkFiller filler;
	private final FrameConfig blocks;
	private final RoomLeafManager leaf;

	public MazeChunkGenerator(Holder<Biome> biome, FrameConfig blocks, RoomLeafManager manager) {
		super(new FixedBiomeSource(biome));
		this.biome = biome;
		this.blocks = blocks;
		this.leaf = manager;
		maze = LazyFunction.create(rand -> new InfiniMaze(new GenerationConfig(SCALE, rand, manager)));
		filler = new ChunkFiller(CELL_WIDTH, SCALE, blocks);
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
		long seed = structures.level instanceof WorldGenLevel sl ? sl.getSeed() : 0;
		return CompletableFuture.supplyAsync(() -> {
			InfiniMaze maze = this.maze.get(seed);
			ChunkPos pos = access.getPos();
			filler.fillChunk(maze, pos, access, ChunkFiller.Step.FRAME);
			return access;
		}, Util.backgroundExecutor());
	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess access, StructureManager structureManager) {
		InfiniMaze maze = this.maze.get(level.getSeed());
		ChunkPos pos = access.getPos();
		filler.fillChunk(maze, pos, access, ChunkFiller.Step.CONTENT);
		super.applyBiomeDecoration(level, access, structureManager);
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor height, RandomState random) {
		return getGenDepth();
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor height, RandomState random) {
		BlockState[] states = new BlockState[height.getHeight()];
		for (int i = 0; i < height.getHeight(); i++) {
			states[i] = blocks.wall();
		}
		return new NoiseColumn(height.getMinBuildHeight(), states);
	}

}
