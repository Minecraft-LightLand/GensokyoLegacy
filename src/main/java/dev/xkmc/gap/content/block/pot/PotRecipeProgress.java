package dev.xkmc.gap.content.block.pot;

import dev.xkmc.gap.content.block.pot.recipe.PotRecipe;
import dev.xkmc.gap.content.block.pot.recipe.RecipeProgressData;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class PotRecipeProgress {

	@SerialField
	public int time, maxTime;

	@SerialField
	public RecipeProgressData data;

	@Nullable
	public PotRecipe<?> recipeCache;

	public PotRecipeProgress() {
	}

	public PotRecipeProgress(RecipeHolder<? extends PotRecipe<?>> holder, RecipeProgressData data) {
		recipeCache = holder.value();
		maxTime = recipeCache.getDuration();
		this.data = data;
	}

	private void validateCache(ServerLevel level, ResourceLocation id) {
		if (recipeCache == null) {
			var recipe = level.getRecipeManager().byKey(id);
			if (recipe.isEmpty()) return;
			if (!(recipe.get().value() instanceof PotRecipe<?> r)) return;
			recipeCache = r;
		}
	}

	public boolean removeOnUpdate(ServerLevel level, PotBlockEntity be, ResourceLocation id) {
		validateCache(level, id);
		if (recipeCache == null) return true;
		if (!recipeCache.mayContinueUse(be.getHeat())) return false;
		time++;
		if (time < maxTime) return false;
		data.execute(be);
		return true;
	}

}
