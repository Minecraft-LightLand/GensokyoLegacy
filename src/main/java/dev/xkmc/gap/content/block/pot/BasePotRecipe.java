package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SerialClass
public abstract class BasePotRecipe<T extends BasePotRecipe<T>> extends BaseRecipe<T, BasePotRecipe<?>, PotRecipeInput> {

    @SerialField
    public PotHeatState heat = PotHeatState.NONE;
    @SerialField
    public PotRecipeTriggerType trigger = PotRecipeTriggerType.NONE;

    public BasePotRecipe(RecType<T, BasePotRecipe<?>, PotRecipeInput> fac) {
        super(fac);
    }

    @Override
    public boolean matches(PotRecipeInput input, Level level) {
        if (input.heat().ordinal() < heat.ordinal()) return false;
        if (input.trigger() != trigger) return false;
        return true;
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
