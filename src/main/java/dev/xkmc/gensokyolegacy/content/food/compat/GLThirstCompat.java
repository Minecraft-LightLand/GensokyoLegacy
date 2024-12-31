package dev.xkmc.gensokyolegacy.content.food.compat;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import dev.xkmc.gensokyolegacy.content.food.reg.GLFood;
import dev.xkmc.gensokyolegacy.content.food.reg.GLSake;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public class GLThirstCompat {

	public static void init() {
		NeoForge.EVENT_BUS.register(GLThirstCompat.class);
	}

	@SubscribeEvent
	public static void onThirstValue(RegisterThirstValueEvent event) {
		for (var e : GLSake.values()) {
			event.addDrink(e.item.get(), 8, 13);
		}
		event.addDrink(GLFood.SCARLET_TEA.item.get(), 8, 13);
	}

}
