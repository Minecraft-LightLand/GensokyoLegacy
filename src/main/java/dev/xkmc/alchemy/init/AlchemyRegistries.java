package dev.xkmc.alchemy.init;

import dev.xkmc.alchemy.content.element.AlchemyElementAxis;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;

public class AlchemyRegistries {

	public static AlchemyElementAxis PYRO, NATURE, EARTH;

	public static void register() {
		var reg = GensokyoLegacy.REGISTRATE;
		PYRO = new AlchemyElementAxis(reg, "pyro", -1, "hydro", -1);
		NATURE = new AlchemyElementAxis(reg, "nature", -1, "metal", -1);
		EARTH = new AlchemyElementAxis(reg, "earth", -1);

	}

}
