package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.l2core.base.tile.BaseContainer;
import net.minecraft.world.item.crafting.RecipeInput;

public class CenserItemContainer extends BaseContainer<CenserItemContainer> implements RecipeInput {

	public CenserItemContainer(int size) {
		super(size);
	}

	@Override
	public int size() {
		return getContainerSize();
	}

	public int ingredientCount() {
		int ans = 0;
		for (var e : getAsList()) {
			if (!e.isEmpty()) {
				ans++;
			}
		}
		return ans;
	}

}
