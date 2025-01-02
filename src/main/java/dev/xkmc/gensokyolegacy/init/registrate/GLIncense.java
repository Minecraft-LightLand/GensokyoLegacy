package dev.xkmc.gensokyolegacy.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.DarknessIncense;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.Incense;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.simple.Val;

public class GLIncense {

	public static final L2Registrate.RegistryInstance<Incense> INCENSE = GensokyoLegacy.REGISTRATE.newRegistry("incense", Incense.class);

	public static final Val<DarknessIncense> DARK = reg("darkness", DarknessIncense::new);

	public static <T extends Incense> Val<T> reg(String id, NonNullSupplier<T> sup) {
		return new Val.Registrate<>(GensokyoLegacy.REGISTRATE.generic(INCENSE, id, sup).register());
	}

	public static void register() {

	}

}
