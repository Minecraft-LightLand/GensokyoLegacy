package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.client.tab.TabRole;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.HasRoleLootCondition;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleCategoryLootCondition;
import dev.xkmc.gensokyolegacy.content.mechanics.role.loot.RoleProgressLootCondition;
import dev.xkmc.gensokyolegacy.content.worldgen.feature.EndIslandData;
import dev.xkmc.gensokyolegacy.content.worldgen.feature.EndIslandFeature;
import dev.xkmc.gensokyolegacy.content.worldgen.infmaze.worldgen.MazeChunkGenerator;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.structure.SetDataProcessor;
import dev.xkmc.l2core.init.reg.simple.CdcReg;
import dev.xkmc.l2core.init.reg.simple.CdcVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.serialization.codec.MapCodecAdaptor;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class GLMisc {

	public static final Val<TabToken<InvTabData, TabRole>> TAB_ROLE = SR.of(GensokyoLegacy.REG, L2Tabs.TABS.reg())
			.reg("role", () -> L2Tabs.GROUP.registerTab(() -> TabRole::new, GLLang.TAB$TITLE.get()));

	private static final SR<StructureProcessorType<?>> PROCESSORS = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_PROCESSOR);
	public static final Val<StructureProcessorType<SetDataProcessor>> SET_DATA = PROCESSORS.reg("set_data", () -> () -> SetDataProcessor.CODEC);

	private static final SR<LootItemConditionType> LIC = SR.of(GensokyoLegacy.REG, Registries.LOOT_CONDITION_TYPE);
	public static final Val<LootItemConditionType> LIC_ROLE = LIC.reg("role",
			() -> new LootItemConditionType(MapCodecAdaptor.of(RoleProgressLootCondition.class)));
	public static final Val<LootItemConditionType> LIC_CATEGORY = LIC.reg("role_category",
			() -> new LootItemConditionType(MapCodecAdaptor.of(RoleCategoryLootCondition.class)));
	public static final Val<LootItemConditionType> LIC_ANY_ROLE = LIC.reg("any_role",
			() -> new LootItemConditionType(MapCodecAdaptor.of(HasRoleLootCondition.class)));

	private static final CdcReg<ChunkGenerator> CGR = CdcReg.of(GensokyoLegacy.REG, BuiltInRegistries.CHUNK_GENERATOR);
	public static final CdcVal<MazeChunkGenerator> CG_MAZE = CGR.reg("maze", MazeChunkGenerator.CODEC);

	private static final SR<Feature<?>> FR = SR.of(GensokyoLegacy.REG, BuiltInRegistries.FEATURE);
	public static final Val<EndIslandFeature> F_ISLAND = FR.reg("island", () -> new EndIslandFeature(EndIslandData.CODEC));


	public static void register() {

	}

}
