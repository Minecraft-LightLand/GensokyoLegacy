package dev.xkmc.gap.content.block.pot;

import dev.xkmc.gap.content.block.pot.recipe.PotRecipeInput;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

@SerialClass
public class PotBlockEntity extends BaseBlockEntity implements BaseContainerListener, TickableBlockEntity, FluidItemTile {

	@SerialField
	public final PotItemHandler items = new PotItemHandler().setMax(1);

	@SerialField
	public final PotTank fluids = new PotTank(1250);

	@SerialField
	public final IngredientHistory history = new IngredientHistory();

	@SerialField
	protected final Map<ResourceLocation, PotRecipeProgress> matchedRecipe = new LinkedHashMap<>();

	private boolean shouldCheckRecipe = true;

	public PotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		items.add(this);
		fluids.add(this);
		fluids.setLevelAccess(() -> level);
	}

	@Override
	public void tick() {
		var level = getLevel();
		if (level instanceof ServerLevel sl) {
			if (shouldCheckRecipe) {
				shouldCheckRecipe = false;
				checkHeatRecipe(sl);
			}
			matchedRecipe.entrySet().removeIf(e -> e.getValue().removeOnUpdate(sl, this, e.getKey()));
		}
	}

	public boolean stir(ServerLevel level) {
		var cont = new PotRecipeInput(this, getHeat(), PotRecipeTriggerType.STIR);
		var opt = level.getRecipeManager().getRecipeFor(GapRegistries.RT_POT.get(), cont, level);
		if (opt.isEmpty()) return false;
		opt.get().value().assemble(cont, level.registryAccess());
		return true;
	}

	public void checkHeatRecipe(ServerLevel level) {
		matchedRecipe.entrySet().removeIf(e -> e.getValue().removeOnValidate(level, this, e.getKey()));
		var cont = new PotRecipeInput(this, getHeat(), PotRecipeTriggerType.STIR);
		var opt = level.getRecipeManager().getRecipeFor(GapRegistries.RT_POT.get(), cont, level);
		if (opt.isEmpty()) return;
		if (matchedRecipe.containsKey(opt.get().id())) return;
		matchedRecipe.put(opt.get().id(), new PotRecipeProgress(opt.get()));
	}

	public PotHeatState getHeat() {
		return PotHeatState.NONE; // TODO
	}

	@Override
	public void notifyTile() {
		shouldCheckRecipe = true;
		sync();
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider pvd) {
		super.loadAdditional(tag, pvd);
		shouldCheckRecipe = true;
	}

	public void dumpInventory() {
		if (level == null) return;
		Containers.dropContents(level, this.getBlockPos().above(), items);
		notifyTile();
	}

	@Override
	public BaseTank getFluidHandler() {
		return fluids;
	}

	@Override
	public SimpleContainer getItemHandler() {
		return items;
	}

}
