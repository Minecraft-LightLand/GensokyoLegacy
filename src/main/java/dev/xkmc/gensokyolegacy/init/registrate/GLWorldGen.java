package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.dimension.EmptyChunkGenerator;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.structure.SetDataProcessor;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class GLWorldGen {

	private static final SR<StructureProcessorType<?>> PROCESSORS = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_PROCESSOR);
	public static final Val<StructureProcessorType<SetDataProcessor>> SET_DATA = PROCESSORS.reg("set_data", () -> () -> SetDataProcessor.CODEC);

	private static final CdcReg<ChunkGenerator> CG = CdcReg.of(GensokyoLegacy.REG, BuiltInRegistries.CHUNK_GENERATOR);
	public static final CdcVal<EmptyChunkGenerator> CG_GAP = CG.reg("gap", EmptyChunkGenerator.CODEC);

	public static void register() {

	}

}
