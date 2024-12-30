package dev.xkmc.gensokyolegacy.init.registrate;

import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class GLSounds {

	public static final SimpleEntry<SoundEvent> KOISHI_RING = reg("koishi_ring");

	private static SimpleEntry<SoundEvent> reg(String id) {
		ResourceLocation rl = GensokyoLegacy.loc(id);
		return new SimpleEntry<>(GensokyoLegacy.REGISTRATE.simple(id, Registries.SOUND_EVENT, () -> SoundEvent.createVariableRangeEvent(rl)));
	}

	public static void register() {
	}

}
