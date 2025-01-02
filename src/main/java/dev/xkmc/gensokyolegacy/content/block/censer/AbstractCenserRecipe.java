package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public abstract class AbstractCenserRecipe<T extends AbstractCenserRecipe<T>> extends BaseRecipe<T, AbstractCenserRecipe<?>, CenserItemContainer> {

	public AbstractCenserRecipe(RecType<T, AbstractCenserRecipe<?>, CenserItemContainer> fac) {
		super(fac);
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
