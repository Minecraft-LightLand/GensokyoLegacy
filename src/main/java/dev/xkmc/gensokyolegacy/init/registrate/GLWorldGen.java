package dev.xkmc.gensokyolegacy.init.registrate;

import com.mojang.serialization.MapCodec;
import dev.xkmc.gensokyolegacy.content.worldgen.feature.FloatingIslandData;
import dev.xkmc.gensokyolegacy.content.worldgen.feature.FloatingIslandFeature;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.init.CellContent;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.MazeChunkGenerator;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.SimpleRoomContent;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.structure.SetDataProcessor;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class GLWorldGen {

	public static final L2Registrate.RegistryInstance<MapCodec<? extends CellContent>> CELL = GensokyoLegacy.REGISTRATE.newRegistry("cell");
	public static final CdcReg<CellContent> CCR = CdcReg.of(GensokyoLegacy.REG, CELL.reg());
	public static final CdcVal<SimpleRoomContent> CC_SIMPLE = CCR.reg("simple", SimpleRoomContent.CODEC);

	private static final SR<StructureProcessorType<?>> PROCESSORS = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_PROCESSOR);
	public static final Val<StructureProcessorType<SetDataProcessor>> SET_DATA = PROCESSORS.reg("set_data", () -> () -> SetDataProcessor.CODEC);

	private static final CdcReg<ChunkGenerator> CGR = CdcReg.of(GensokyoLegacy.REG, BuiltInRegistries.CHUNK_GENERATOR);
	public static final CdcVal<MazeChunkGenerator> CG_MAZE = CGR.reg("maze", MazeChunkGenerator.CODEC);

	private static final SR<Feature<?>> FR = SR.of(GensokyoLegacy.REG, BuiltInRegistries.FEATURE);
	public static final Val<FloatingIslandFeature> F_ISLAND = FR.reg("island", () -> new FloatingIslandFeature(FloatingIslandData.CODEC));

	public static void register() {

	}

}
