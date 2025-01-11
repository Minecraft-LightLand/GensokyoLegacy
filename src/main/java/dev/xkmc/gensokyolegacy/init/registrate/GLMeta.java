package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterAttachment;
import dev.xkmc.gensokyolegacy.content.attachment.chunk.StructureAttachment;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.StructureConfig;
import dev.xkmc.gensokyolegacy.content.attachment.misc.FrogGodCapability;
import dev.xkmc.gensokyolegacy.content.attachment.misc.KoishiAttackCapability;
import dev.xkmc.gensokyolegacy.content.attachment.role.PlayerRolePlayAttachment;
import dev.xkmc.gensokyolegacy.content.block.ritual.BlockMana;
import dev.xkmc.gensokyolegacy.content.client.tab.TabRole;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.gensokyolegacy.init.data.structure.SetDataProcessor;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2serial.serialization.codec.CodecAdaptor;
import dev.xkmc.l2tabs.init.L2Tabs;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class GLMeta {

	private static final AttReg ATT = AttReg.of(GensokyoLegacy.REG);

	public static final AttVal.PlayerVal<CharacterAttachment> CHAR = ATT.player("character_data",
			CharacterAttachment.class, CharacterAttachment::new, PlayerCapabilityNetworkHandler::new);
	public static final AttVal.PlayerVal<PlayerRolePlayAttachment> ABILITY = ATT.player("role_play",
			PlayerRolePlayAttachment.class, PlayerRolePlayAttachment::new, PlayerCapabilityNetworkHandler::new);
	public static final AttVal.CapVal<LevelChunk, StructureAttachment> STRUCTURE = ATT.entity("structure_data",
			StructureAttachment.class, StructureAttachment::new, LevelChunk.class, e -> true);

	public static final AttVal.PlayerVal<KoishiAttackCapability> KOISHI_ATTACK = ATT.player("koishi_attack",
			KoishiAttackCapability.class, KoishiAttackCapability::new, PlayerCapabilityNetworkHandler::new);
	public static final AttVal.CapVal<Frog, FrogGodCapability> FROG_GOD = ATT.entity("frog_god",
			FrogGodCapability.class, FrogGodCapability::new, Frog.class, e -> true);

	public static final DataMapReg<Block, BedData> BED_DATA =
			GensokyoLegacy.REG.dataMap("bed_data", Registries.BLOCK, BedData.class);
	public static final DataMapReg<EntityType<?>, CharacterConfig> ENTITY_DATA =
			GensokyoLegacy.REG.dataMap("character_config", Registries.ENTITY_TYPE, CharacterConfig.class);
	public static final DataMapReg<Structure, StructureConfig> STRUCTURE_DATA =
			GensokyoLegacy.REG.dataMap(DataMapType.builder(GensokyoLegacy.loc("structure_config"),
					Registries.STRUCTURE, new CodecAdaptor<>(StructureConfig.class)).build());
	public static final DataMapReg<Block, BlockMana> BLOCK_MANA =
			GensokyoLegacy.REG.dataMap("block_mana", Registries.BLOCK, BlockMana.class);

	private static final SR<StructureProcessorType<?>> PROCESSORS = SR.of(GensokyoLegacy.REG, Registries.STRUCTURE_PROCESSOR);
	public static final Val<StructureProcessorType<SetDataProcessor>> SET_DATA = PROCESSORS.reg("set_data", () -> () -> SetDataProcessor.CODEC);

	public static final Val<TabToken<InvTabData, TabRole>> TAB_ATTRIBUTE = SR.of(GensokyoLegacy.REG, L2Tabs.TABS.reg())
			.reg("role", () -> L2Tabs.GROUP.registerTab(() -> TabRole::new, GLLang.TAB$TITLE.get()));

	public static void register() {

	}

}
