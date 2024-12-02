package dev.xkmc.gensokyolegacy.content.entity.behavior.combat;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class YoukaiFightEvent extends Event implements ICancellableEvent {

	public final YoukaiEntity youkai;
	public final LivingEntity target;

	public YoukaiFightEvent(YoukaiEntity youkai, LivingEntity target) {
		this.youkai = youkai;
		this.target = target;
	}

}
