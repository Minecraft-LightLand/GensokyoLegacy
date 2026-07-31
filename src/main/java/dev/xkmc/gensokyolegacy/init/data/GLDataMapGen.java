package dev.xkmc.gensokyolegacy.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.gensokyolegacy.init.data.structure.GLStructureGen;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import dev.xkmc.l2tabs.init.L2Tabs;
import net.minecraft.world.item.Items;

public class GLDataMapGen {

	public static void dataMapGen(RegistrateDataMapProvider pvd) {
		GLStructureGen.dataMap(pvd);
		pvd.builder(L2Tabs.ICON.reg())
				.add(GLMisc.QUEST_TAB.id(), Items.BOOK, false);
		pvd.builder(L2Tabs.ORDER.reg())
				.add(GLMisc.QUEST_TAB.id(), 3750, false);
	}

}
