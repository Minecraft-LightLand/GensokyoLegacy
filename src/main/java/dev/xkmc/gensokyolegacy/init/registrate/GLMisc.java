package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.attachment.character.CharacterAttachment;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.BedData;
import dev.xkmc.gensokyolegacy.content.attachment.datamap.CharacterConfig;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public class GLMisc {

	private static final AttReg ATT = AttReg.of(GensokyoLegacy.REG);

	public static final AttVal.PlayerVal<CharacterAttachment> CHAR = ATT.player("character_data",
			CharacterAttachment.class, CharacterAttachment::new, PlayerCapabilityNetworkHandler::new);

	public static final DataMapReg<Block, BedData> BED_DATA =
			GensokyoLegacy.REG.dataMap("bed_data", Registries.BLOCK, BedData.class);
	public static final DataMapReg<EntityType<?>, CharacterConfig> ENTITY_DATA =
			GensokyoLegacy.REG.dataMap("character_config", Registries.ENTITY_TYPE, CharacterConfig.class);

}
