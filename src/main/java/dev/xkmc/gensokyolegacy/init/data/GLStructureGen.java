package dev.xkmc.gensokyolegacy.init.data;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.util.Lazy;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class GLStructureGen {

	private record GLStructure(
			ResourceLocation id, TagKey<Biome> biomes, int spacing, int separation,
			StructureConfig.Builder config,
			List<StructureProcessor> processors,
			List<GLBeds> beds,
			Map<MobCategory, StructureSpawnOverride> spawns) {
	}

	private record GLBeds(
			Holder<EntityType<?>> entity,
			Holder<Block> bed,
			CharacterConfig data
	) {
	}

	private static List<GLStructure> initStructures() {
		var cirno = GensokyoLegacy.loc("cirno_nest");
		return List.of(
				new GLStructure(cirno, GLTagGen.CIRNO_NEST, 24, 8,
						StructureConfig.builder().room(1, 1, 1),
						List.of(
								new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
								new RuleProcessor(List.of(
										injectData(Blocks.DECORATED_POT, GLLootGen.CIRNO_POT),
										injectData(ModBlocks.SPRUCE_CABINET.get(), GLLootGen.CIRNO_CABINET),
										injectData(ModBlocks.BASKET.get(), GLLootGen.CIRNO_BASKET),
										injectData(Blocks.BARREL, GLLootGen.CIRNO_BARREL)
								))
						),
						List.of(
								new GLBeds(GLEntities.CIRNO, GLBlocks.Beds.CIRNO.holder(),
										new CharacterConfig(cirno, 12000, 24000,
												16, 1, 1, 600))
						),
						Map.of())
		);
	}

	private static final Supplier<List<GLStructure>> STRUCTURES = Lazy.of(GLStructureGen::initStructures);

	public static void dataMap(RegistrateDataMapProvider pvd) {
		var bed = pvd.builder(GLMisc.BED_DATA.reg());
		var entity = pvd.builder(GLMisc.ENTITY_DATA.reg());
		var structure = pvd.builder(GLMisc.STRUCTURE_DATA.reg());
		for (var e : STRUCTURES.get()) {
			var config = e.config();
			for (var beds : e.beds) {
				bed.add(beds.bed(), new BedData(beds.entity.value()), false);
				entity.add(beds.entity(), beds.data, false);
				config.addEntity(beds.entity.value());
			}
			structure.add(e.id(), config.build(), false);
		}
	}

	public static void init(DataProviderInitializer init) {
		init.add(Registries.PROCESSOR_LIST, ctx -> {
			for (var e : STRUCTURES.get()) {
				ctx.register(ResourceKey.create(Registries.PROCESSOR_LIST, e.id),
						new StructureProcessorList(e.processors()));
			}
		});
		init.add(Registries.TEMPLATE_POOL, ctx -> {
			var empty = ctx.lookup(Registries.TEMPLATE_POOL)
					.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, ResourceLocation.withDefaultNamespace("empty")));
			for (var e : STRUCTURES.get()) {
				var list = ctx.lookup(Registries.PROCESSOR_LIST)
						.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, e.id()));
				ctx.register(ResourceKey.create(Registries.TEMPLATE_POOL, e.id()), new StructureTemplatePool(empty, List.of(
						Pair.of(new SinglePiece(e.id(), list, StructureTemplatePool.Projection.RIGID), 1)
				)));
			}
		});
		init.add(Registries.STRUCTURE, ctx -> {
			for (var e : STRUCTURES.get()) {
				var biome = ctx.lookup(Registries.BIOME).getOrThrow(e.biomes());
				var pool = ctx.lookup(Registries.TEMPLATE_POOL)
						.getOrThrow(ResourceKey.create(Registries.TEMPLATE_POOL, e.id()));
				ctx.register(ResourceKey.create(Registries.STRUCTURE, e.id()), new JigsawStructure(
						new Structure.StructureSettings(biome, e.spawns(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
						pool, 1, ConstantHeight.of(VerticalAnchor.absolute(0)), false, Heightmap.Types.WORLD_SURFACE_WG)
				);
			}
		});
		init.add(Registries.STRUCTURE_SET, ctx -> {
			for (var e : STRUCTURES.get()) {
				var str = ctx.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, e.id));
				ctx.register(ResourceKey.create(Registries.STRUCTURE_SET, e.id), new StructureSet(
						str, new RandomSpreadStructurePlacement(e.spacing(), e.separation(), RandomSpreadType.LINEAR, e.id.hashCode() & 0x7fffffff)));
			}
		});
	}

	private static ProcessorRule injectData(Block block, ResourceKey<LootTable> table) {
		return new ProcessorRule(new BlockMatchTest(block), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				block.defaultBlockState(), new AppendLoot(table));
	}

	private static class SinglePiece extends SinglePoolElement {

		protected SinglePiece(ResourceLocation template, Holder<StructureProcessorList> list, StructureTemplatePool.Projection proj) {
			super(Either.left(template), list, proj, Optional.of(LiquidSettings.IGNORE_WATERLOGGING));
		}

	}

}
