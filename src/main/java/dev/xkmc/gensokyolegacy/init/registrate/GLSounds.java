package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.youkaishomecoming.init.YoukaisHomecoming;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GLSounds {

	public static final SimpleEntry<SoundEvent> KOISHI_RING = reg("koishi_ring");

	private static SimpleEntry<SoundEvent> reg(String id) {
		ResourceLocation rl = YoukaisHomecoming.loc(id);
		return new SimpleEntry<>(YoukaisHomecoming.REGISTRATE.simple(id, Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(rl)));
	}

	public static void register() {
	}

}
