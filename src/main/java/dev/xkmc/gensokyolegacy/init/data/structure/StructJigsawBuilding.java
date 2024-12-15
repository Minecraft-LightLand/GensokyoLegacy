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

public record StructJigsawBuilding(
		int maxDepth,
		List<StructJigsawBuilding.Part> parts,
		Map<MobCategory, StructureSpawnOverride> spawns
) implements StructBuilding {

	public record Part(
			String id, boolean isRoad,
			List<StructureProcessor> processors

	) {

	}

	@Override
	public void registerTemplatePools(BootstrapContext<StructureTemplatePool> ctx, ResourceLocation id) {
		for (var e : parts) {
			var pid = id.withSuffix("/" + e.id);
			var empty = ctx.lookup(Registries.TEMPLATE_POOL)
					.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.withDefaultNamespace("empty")));
			var list = ctx.lookup(Registries.PROCESSOR_LIST)
					.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, pid));
			var proj = e.isRoad() ? StructureTemplatePool.Projection.TERRAIN_MATCHING : StructureTemplatePool.Projection.RIGID;
			ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, pid), new StructureTemplatePool(empty, List.of(
					Pair.of(new GLSinglePiece(pid, list, proj), 1)
			)));
		}

	}

	@Override
	public void registerProcessors(BootstrapContext<StructureProcessorList> ctx, ResourceLocation id) {
		for (var e : parts) {
			var pid = id.withSuffix("/" + e.id);
			ctx.register(ResourceKey.create(Registries.PROCESSOR_LIST, pid),
					new StructureProcessorList(e.processors()));
		}
	}

	@Override
	public void registerStructure(BootstrapContext<Structure> ctx, ResourceLocation id, HolderSet.Named<Biome> biome) {
		var pool = ctx.lookup(Registries.TEMPLATE_POOL)
				.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, id.withSuffix("/" + parts().getFirst().id())));
		ctx.register(ResourceKey.create(Registries.STRUCTURE, id), new JigsawStructure(
				new Structure.StructureSettings(biome, spawns(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				pool, maxDepth(), ConstantHeight.of(VerticalAnchor.absolute(0)), false, Heightmap.Types.WORLD_SURFACE_WG)
		);
	}
}
