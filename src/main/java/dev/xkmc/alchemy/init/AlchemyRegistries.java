package dev.xkmc.alchemy.init;

import dev.xkmc.alchemy.content.element.AlchemyElement;
import dev.xkmc.gensokyolegacy.init.GensokyoLegacy;

public class AlchemyRegistries {

	public static AlchemyElement PYRO, NATURE, EARTH;

	public static void register() {
		var reg = GensokyoLegacy.REGISTRATE;
		PYRO = new AlchemyElement(reg, "pyro", -1, "hydro", -1);
		NATURE = new AlchemyElement(reg, "nature", -1, "metal", -1);
		EARTH = new AlchemyElement(reg, "earth", -1);

	}

}
