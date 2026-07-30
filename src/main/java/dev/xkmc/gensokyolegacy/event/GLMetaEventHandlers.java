package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.rpg.core.ServerCharacterDialogManager;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GLMetaEventHandlers {

	@SubscribeEvent
	public static void reload(OnDatapackSyncEvent event) {
		if (event.getPlayer() == null) {
			ServerCharacterDialogManager.clearCache();
		}
	}

}
