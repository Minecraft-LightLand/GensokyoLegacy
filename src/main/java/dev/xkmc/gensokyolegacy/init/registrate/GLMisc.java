package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.content.attachment.CharacterAttachment;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;

public class GLMisc {

	private static final AttReg ATT = AttReg.of(GensokyoLegacy.REG);

	public static final AttVal.PlayerVal<CharacterAttachment> CHAR = ATT.player("character_data",
			CharacterAttachment.class, CharacterAttachment::new, PlayerCapabilityNetworkHandler::new);

}
