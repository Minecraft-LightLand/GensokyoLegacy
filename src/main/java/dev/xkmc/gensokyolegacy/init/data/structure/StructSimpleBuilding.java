package dev.xkmc.gensokyolegacy.init.data.structure;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;
import java.util.Map;

public record StructSimpleBuilding(
		List<StructureProcessor> processors,
		Map<MobCategory, StructureSpawnOverride> spawns
) implements StructBuilding {

	@Override
	public void registerTemplatePools(BootstrapContext<StructureTemplatePool> ctx, ResourceLocation id) {
		var empty = ctx.lookup(Registries.TEMPLATE_POOL)
				.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.withDefaultNamespace("empty")));
		var list = ctx.lookup(Registries.PROCESSOR_LIST)
				.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, id));
		ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, id), new StructureTemplatePool(empty, List.of(
				Pair.of(new GLSinglePiece(id, list, StructureTemplatePool.Projection.RIGID), 1)
		)));
	}

	@Override
	public void registerProcessors(BootstrapContext<StructureProcessorList> ctx, ResourceLocation id) {
		ctx.register(ResourceKey.create(Registries.PROCESSOR_LIST, id),
				new StructureProcessorList(processors()));
	}

	@Override
	public void registerStructure(BootstrapContext<Structure> ctx, ResourceLocation id, HolderSet.Named<Biome> biome) {
		var pool = ctx.lookup(Registries.TEMPLATE_POOL)
				.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id));
		ctx.register(ResourceKey.create(Registries.STRUCTURE, id), new JigsawStructure(
				new Structure.StructureSettings(biome, spawns(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				pool, 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Heightmap.Types.WORLD_SURFACE_WG)
		);
	}
}
