package dev.xkmc.alchemy.init;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;

public class AlchemyDataGen {

	public static void dataMap(RegistrateDataMapProvider pvd) {
		AlchemyRegistries.PYRO.regDataMap(pvd);
		AlchemyRegistries.NATURE.regDataMap(pvd);
		AlchemyRegistries.EARTH.regDataMap(pvd);
	}

}
