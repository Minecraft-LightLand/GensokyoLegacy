package dev.xkmc.gap.content.block.pot;

import dev.xkmc.gap.content.block.bellow.BellowBlockEntity;
import dev.xkmc.gap.content.block.pot.recipe.PotRecipeInput;
import dev.xkmc.gap.init.registrate.GapRegistries;
import dev.xkmc.gap.init.registrate.GapTagGen;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2core.base.tile.BaseTank;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
	private PotHeatState heatCache = null;

	public PotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		items.add(this);
		fluids.add(this);
		fluids.setLevelAccess(() -> level);
	}

	@Override
	public void tick() {
		heatCache = null;
		heatCache = getHeat();
		var level = getLevel();
		if (level instanceof ServerLevel sl) {
			if (shouldCheckRecipe) {
				shouldCheckRecipe = false;
				checkHeatRecipe(sl);
			}
			if (!matchedRecipe.isEmpty()) {
				matchedRecipe.entrySet().removeIf(e -> e.getValue().removeOnUpdate(sl, this, e.getKey()));
			}
		}
	}

	public boolean stir(ServerLevel level) {
		var cont = new PotRecipeInput(this, getHeat(), PotRecipeTriggerType.STIR);
		var opt = level.getRecipeManager().getRecipeFor(GapRegistries.RT_POT.get(), cont, level);
		if (opt.isEmpty()) return false;
		var data = opt.get().value().start(cont, level.registryAccess());
		if (opt.get().value().getDuration() > 0)
			matchedRecipe.put(opt.get().id(), new PotRecipeProgress(opt.get(), data));
		else data.execute(this);
		return true;
	}

	public void checkHeatRecipe(ServerLevel level) {
		var cont = new PotRecipeInput(this, getHeat(), PotRecipeTriggerType.STIR);
		var opt = level.getRecipeManager().getRecipeFor(GapRegistries.RT_POT.get(), cont, level);
		if (opt.isEmpty()) return;
		if (matchedRecipe.containsKey(opt.get().id())) return;
		matchedRecipe.put(opt.get().id(), new PotRecipeProgress(opt.get(), opt.get().value().start(cont, level.registryAccess())));
	}

	public PotHeatState getHeat() {
		if (heatCache != null) return heatCache;
		if (level == null) return PotHeatState.NONE;
		var below = level.getBlockState(getBlockPos().below());
		if (!below.is(GapTagGen.HEAT_SOURCE)) return PotHeatState.NONE;
		for (int i = 0; i < 4; i++) {
			var dir = Direction.from2DDataValue(i);
			var next = getBlockPos().below().relative(dir);
			if (level.getBlockEntity(next) instanceof BellowBlockEntity be && be.isBlowing() &&
					be.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING) == dir.getOpposite()
			) {
				return PotHeatState.BOILING;
			}
		}
		return PotHeatState.HEATED;
	}

	@Override
	public boolean mayModify() {
		return matchedRecipe.isEmpty();
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
		history.clear();
		matchedRecipe.clear();
		notifyTile();
	}

	@Override
	public ItemStack addItem(ItemStack stack) {
		var copy = stack.copy();
		var ans = FluidItemTile.super.addItem(stack);
		copy.shrink(ans.getCount());
		if (!copy.isEmpty()) {
			history.add(copy);
		}
		return ans;
	}

	@Override
	public BaseTank getFluidHandler() {
		return fluids;
	}

	@Override
	public SimpleContainer getItemHandler() {
		return items;
	}

	public List<ItemStack> getItemsForRender() {
		List<ItemStack> ans = new ArrayList<>();
		for (var e : items.getAsList()) {
			if (!e.isEmpty())
				ans.add(e);
		}
		for (var ent : matchedRecipe.values()) {
			for (var e : ent.data.consumedItems()) {
				if (!e.isEmpty())
					ans.add(e);
			}
		}
		return ans;
	}

	public List<FluidStack> getFluidsForRender() {
		List<FluidStack> ans = new ArrayList<>();
		for (var e : fluids.getAsList()) {
			if (e.isEmpty()) continue;
			var cap = fluids.getCap(e);
			if (cap != null && cap.hide()) continue;
			ans.add(e);
		}
		for (var ent : matchedRecipe.values()) {
			for (var e : ent.data.consumedFluids()) {
				if (e.isEmpty()) continue;
				var cap = fluids.getCap(e);
				if (cap != null && cap.hide()) continue;
				ans.add(e);
			}
		}
		return ans;
	}

}
