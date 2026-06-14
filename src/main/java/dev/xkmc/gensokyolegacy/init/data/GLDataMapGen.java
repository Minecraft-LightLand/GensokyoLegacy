package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureGen;

public class GLDataMapGen {

	public static void dataMapGen(RegistrateDataMapProvider pvd) {
		GLStructureGen.dataMap(pvd);
	}

}
