package dev.xkmc.gensokyolegacy.content.block.ritual;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public abstract class RitualRecipe<T extends RitualRecipe<T>> extends BaseRecipe<T, RitualRecipe<?>, RitualInput> {

	public RitualRecipe(RecType<T, RitualRecipe<?>, RitualInput> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
