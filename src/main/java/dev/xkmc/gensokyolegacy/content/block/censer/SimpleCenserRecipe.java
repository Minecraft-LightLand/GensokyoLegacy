package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.Incense;
import dev.xkmc.gensokyolegacy.init.registrate.GLItems;
import dev.xkmc.gensokyolegacy.init.registrate.GLRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;

@SerialClass
public class SimpleCenserRecipe extends AbstractCenserRecipe<SimpleCenserRecipe> {

	@SerialField
	public ArrayList<Ingredient> ingredients = new ArrayList<>();
	@SerialField
	public Incense result;
	@SerialField
	public int duration;

	public SimpleCenserRecipe() {
		super(GLRecipes.RS_CENSER.get());
	}

	@Override
	public boolean matches(CenserItemContainer cont, Level level) {
		if (cont.ingredientCount() != this.ingredients.size()) {
			return false;
		} else {
			var nonEmptyItems = new java.util.ArrayList<ItemStack>(cont.ingredientCount());
			for (var item : cont.getAsList())
				if (!item.isEmpty())
					nonEmptyItems.add(item);
			return RecipeMatcher.findMatches(nonEmptyItems, this.ingredients) != null;
		}
	}

	@Override
	public ItemStack assemble(CenserItemContainer cont, HolderLookup.Provider provider) {
		return GLItems.INCENSE_DUR.set(result.asStack(), duration);
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return GLItems.INCENSE_DUR.set(result.asStack(), duration);
	}

}
