package dev.xkmc.gap.content.block.pot.recipe;

import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public class SimplePotRecipe extends FluidItemPotRecipe<SimplePotRecipe> {

	public SimplePotRecipe() {
		super(GapRegistries.RS_POT.get());
	}

}
