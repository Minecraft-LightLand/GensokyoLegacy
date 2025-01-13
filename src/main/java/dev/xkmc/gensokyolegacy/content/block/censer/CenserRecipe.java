package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public abstract class CenserRecipe<T extends CenserRecipe<T>> extends BaseRecipe<T, CenserRecipe<?>, CenserItemContainer> {

	public CenserRecipe(RecType<T, CenserRecipe<?>, CenserItemContainer> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
