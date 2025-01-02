package dev.xkmc.gensokyolegacy.content.block.censer;

import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.Incense;
import dev.xkmc.gensokyolegacy.content.mechanics.incense.core.IncenseItem;
import dev.xkmc.gensokyolegacy.init.registrate.GLRecipes;
import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2core.base.tile.BaseContainerListener;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2modularblock.tile_api.TickableBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.youkaishomecoming.content.pot.overlay.InfoTile;
import dev.xkmc.youkaishomecoming.content.pot.overlay.TileTooltip;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class CenserBlockEntity extends BaseBlockEntity
		implements BlockContainer, BaseContainerListener, TickableBlockEntity, InfoTile {

	@SerialField
	public CenserItemContainer items = new CenserItemContainer(9).setMax(1).add(this);

	private final IItemHandler itemHandler;
	@SerialField
	private int incenseRemainingDuration, incenseRadius;
	@SerialField
	private Incense incense;

	public CenserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemHandler = new InvWrapper(items);
	}

	@Override
	public void tick() {
		if (level == null) return;
		if (incense != null) {
			if (getBlockState().getValue(CenserBlock.LIT) > 0) {
				incense.tick(level, getBlockPos(), incenseRadius);
				incenseRemainingDuration--;
			}
			if (incenseRemainingDuration <= 0) {
				incense = null;
				incenseRemainingDuration = 0;
				level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CenserBlock.LIT, 0));
			}
		}
	}

	@Nullable
	public Incense getIncense() {
		return incense;
	}

	public int tryLit(boolean simulate) {
		if (level == null) return 0;
		if (incense != null) return incense.getBrightness(level);
		var ans = level.getRecipeManager().getRecipeFor(GLRecipes.RT_CENSER.get(), items, level);
		if (!simulate && ans.isPresent()) {
			var stack = ans.get().value().assemble(items, level.registryAccess());
			for (var e : items.getAsList()) {
				e.shrink(1);
			}
			applyIncense(stack);
		}
		return ans.isPresent() && ans.get().value().getResultItem(level.registryAccess()).getItem() instanceof IncenseItem item ?
				item.get().getBrightness(level) : 0;
	}

	public void applyIncense(ItemStack stack) {
		if (level == null) return;
		if (!(stack.getItem() instanceof IncenseItem item)) return;
		incense = item.get();
		incenseRemainingDuration = item.getIncenseDuration(level, stack);
		incenseRadius = item.getIncenseRadius(level, stack);
	}

	public boolean tryAddItem(ItemStack stack) {
		if (level == null) return false;
		if (stack.getItem() instanceof IncenseItem) {
			applyIncense(stack);
		}
		if (incense != null) return false;
		for (int i = 0; i < items.size(); i++) {
			if (items.getItem(i).isEmpty()) {
				if (!level.isClientSide())
					items.setItem(i, stack.split(1));
				return true;
			}
		}
		return false;
	}

	public List<Container> getContainers() {
		return List.of(this.items);
	}

	public void popItems() {
		if (this.level != null) {
			incense = null;
			incenseRemainingDuration = 0;
			Containers.dropContents(this.level, this.getBlockPos().above(), this.items);
			level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(CenserBlock.LIT, 0));
			this.notifyTile();
		}
	}

	public void notifyTile() {
		this.setChanged();
		this.sync();
	}

	@Override
	public TileTooltip getImage() {
		if (incense != null) {
			return new TileTooltip(List.of(incense.asStack()), List.of());
		}
		return new TileTooltip(items.getAsList(), List.of());
	}

	@Override
	public List<Component> lines() {
		return List.of();//TODO
	}

	public IItemHandler getItemHandler(@Nullable Direction direction) {
		return this.itemHandler;
	}

}
