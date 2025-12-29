package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.entity.characters.rumia.RumiaEntity;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GLGeneralEventHandlers {

	@SubscribeEvent
	public static void onShieldBlock(LivingShieldBlockEvent event) {
		if (event.getBlocked() && event.getDamageSource().getDirectEntity() instanceof RumiaEntity rumia) {
			rumia.state.onBlocked();
		}
	}

}