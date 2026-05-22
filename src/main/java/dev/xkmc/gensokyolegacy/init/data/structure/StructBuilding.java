package dev.xkmc.gensokyolegacy.init.data.structure;

import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

public interface StructBuilding {

	void registerTemplatePools(BootstrapContext<StructureTemplatePool> ctx, Identifier id);

	void registerProcessors(BootstrapContext<StructureProcessorList> ctx, Identifier id);

	void registerStructure(BootstrapContext<Structure> ctx, Identifier id, HolderSet.Named<Biome> biome);

}
