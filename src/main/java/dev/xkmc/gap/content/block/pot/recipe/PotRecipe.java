package dev.xkmc.gap.content.block.pot.recipe;

import dev.xkmc.gap.content.block.pot.PotHeatState;
import dev.xkmc.gap.content.block.pot.PotRecipeTriggerType;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SerialClass
public abstract class PotRecipe<T extends PotRecipe<T>> extends BaseRecipe<T, PotRecipe<?>, PotRecipeInput> {

	@SerialField
	public PotHeatState heat = PotHeatState.NONE;
	@SerialField
	public PotRecipeTriggerType trigger = PotRecipeTriggerType.NONE;
	@SerialField
	public int duration;

	public PotRecipe(RecType<T, PotRecipe<?>, PotRecipeInput> fac) {
		super(fac);
	}

	public boolean mayContinueUse(PotHeatState input) {
		return input.ordinal() >= heat.ordinal();
	}

	@Override
	public boolean matches(PotRecipeInput input, Level level) {
		if (input.heat().ordinal() < heat.ordinal()) return false;
		return input.trigger() != PotRecipeTriggerType.ALL && input.trigger() == trigger;
	}

	@Override
	public ItemStack assemble(PotRecipeInput input, HolderLookup.Provider pvd) {
		return ItemStack.EMPTY;
	}

	public abstract RecipeProgressData start(PotRecipeInput input, HolderLookup.Provider pvd) ;

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pvd) {
		return ItemStack.EMPTY;
	}

	public int getDuration() {
		return duration;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

}
