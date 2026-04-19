package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;

@SerialClass
public abstract class BasePotRecipe<T extends BasePotRecipe<T>> extends BaseRecipe<T, BasePotRecipe<?>, PotRecipeInput> {

    public BasePotRecipe(RecType<T, BasePotRecipe<?>, PotRecipeInput> fac) {
        super(fac);
    }

    @Override
    public ItemStack assemble(PotRecipeInput input, HolderLookup.Provider pvd) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pvd) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

}
