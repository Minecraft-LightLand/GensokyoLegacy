package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.rpg.trigger.KillTrigger;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.gensokyolegacy.init.registrate.GLMeta;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

@EventBusSubscriber(modid = GensokyoLegacy.MODID)
public class GLQuestEventHandlers {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void reload(LivingDeathEvent event) {
		if (event.getEntity().getKillCredit() instanceof ServerPlayer sp) {
			GLMeta.QUEST.type().getOrCreate(sp).dispatch(sp, new KillTrigger(sp, event.getEntity()));
		}
	}

}
