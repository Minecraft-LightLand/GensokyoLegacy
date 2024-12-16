package dev.xkmc.gensokyolegacy.init.data.structure;

import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLBlocks;
import dev.xkmc.gensokyolegacy.init.registrate.GLEntities;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.youkaishomecoming.init.data.YHBiomeTagsProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.util.Lazy;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GLStructureGen {

	private static List<StructStructure> initStructures() {
		StructStructure cirno, reimu;
		{
			cirno = new StructStructure(GensokyoLegacy.loc("cirno_nest"),
					GLStructureTagGen.CIRNO_NEST, 24, 8,
					StructureConfig.builder().room(1, 1, 1)
							.primary(GLStructureTagGen.CIRNO_PRIMARY)
							.wouldFix(GLStructureTagGen.CIRNO_FIX),
					List.of(
							new StructBed(GLEntities.CIRNO, GLBlocks.Beds.CIRNO.holder(),
									CharacterConfig.forStructure(12000, 18000, 8, 600))
					),
					new StructSimpleBuilding(
							List.of(
									new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
									new RuleProcessor(List.of(
											injectData(Blocks.DECORATED_POT, GLStructureLootGen.CIRNO_POT),
											injectData(ModBlocks.SPRUCE_CABINET.get(), GLStructureLootGen.CIRNO_CABINET),
											injectData(ModBlocks.BASKET.get(), GLStructureLootGen.CIRNO_BASKET),
											injectData(Blocks.BARREL, GLStructureLootGen.CIRNO_BARREL)
									))
							), Map.of()
					)
			);
		}

		{
			reimu = new StructStructure(GensokyoLegacy.loc("hakurei_shrine"),
					YHBiomeTagsProvider.HAS_SHRINE, 24, 8,
					StructureConfig.builder().room(7, 5, 2)
							.house(0, 0, 1)
							.primary(GLStructureTagGen.REIMU_PRIMARY)
							.wouldFix(GLStructureTagGen.REIMU_FIX),
					List.of(
							new StructBed(GLEntities.REIMU, GLBlocks.Beds.REIMU.holder(),
									CharacterConfig.forStructure(12000, 24000, 8, 600))
					),
					new StructJigsawBuilding(2, List.of(
							new StructJigsawBuilding.Part("shrine", false, List.of(
									new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE),
									new RuleProcessor(List.of(
											injectData(Blocks.CHEST, GLStructureLootGen.SHRINE_CHEST),
											injectData(Blocks.BARREL, GLStructureLootGen.SHRINE_BARREL),
											injectData(ModBlocks.SPRUCE_CABINET.get(), GLStructureLootGen.SHRINE_CABINET)
									)))),
							new StructJigsawBuilding.Part("storage", false, List.of(
									new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE))),
							new StructJigsawBuilding.Part("path", false, List.of(
									new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE))),
							new StructJigsawBuilding.Part("gate", false, List.of(
									new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)))
					), Map.of())
			);
		}
		return List.of(cirno, reimu);
	}

	private static final Supplier<List<StructStructure>> STRUCTURES = Lazy.of(GLStructureGen::initStructures);

	public static void dataMap(RegistrateDataMapProvider pvd) {
		var bed = pvd.builder(GLMisc.BED_DATA.reg());
		var entity = pvd.builder(GLMisc.ENTITY_DATA.reg());
		var structure = pvd.builder(GLMisc.STRUCTURE_DATA.reg());
		for (var e : STRUCTURES.get()) {
			var config = e.config();
			for (var beds : e.beds()) {
				bed.add(beds.bed(), new BedData(beds.entity().value()), false);
				entity.add(beds.entity(), beds.data().withId(e.id()), false);
				config.addEntity(beds.entity().value());
			}
			structure.add(e.id(), config.build(), false);
		}
	}

	public static void init(DataProviderInitializer init) {
		init.add(Registries.PROCESSOR_LIST, ctx -> {
			for (var e : STRUCTURES.get()) {
				e.building().registerProcessors(ctx, e.id());
			}
		});
		init.add(Registries.TEMPLATE_POOL, ctx -> {
			for (var e : STRUCTURES.get()) {
				e.building().registerTemplatePools(ctx, e.id());
			}
		});
		init.add(Registries.STRUCTURE, ctx -> {
			for (var e : STRUCTURES.get()) {
				var biome = ctx.lookup(Registries.BIOME).getOrThrow(e.biomes());
				e.building().registerStructure(ctx, e.id(), biome);
			}
		});
		init.add(Registries.STRUCTURE_SET, ctx -> {
			for (var e : STRUCTURES.get()) {
				var str = ctx.lookup(Registries.STRUCTURE).getOrThrow(ResourceKey.create(Registries.STRUCTURE, e.id()));
				ctx.register(ResourceKey.create(Registries.STRUCTURE_SET, e.id()), new StructureSet(
						str, new RandomSpreadStructurePlacement(e.spacing(), e.separation(), RandomSpreadType.LINEAR, e.id().hashCode() & 0x7fffffff)));
			}
		});
	}

	private static ProcessorRule injectData(Block block, ResourceKey<LootTable> table) {
		return new ProcessorRule(new BlockMatchTest(block), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE,
				block.defaultBlockState(), new AppendLoot(table));
	}

}
