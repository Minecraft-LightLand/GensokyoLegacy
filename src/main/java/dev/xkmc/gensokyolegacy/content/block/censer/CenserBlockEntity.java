package dev.xkmc.gensokyolegacy.content.block.censer;

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
import net.minecraft.resources.ResourceLocation;
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
	public CenserItemContainer items = new CenserItemContainer(3).setMax(1).add(this);

	private final IItemHandler itemHandler;
	@SerialField
	private int totalTime;
	@SerialField
	private int fermentationProgress;
	@SerialField
	private ResourceLocation recipeId;
	private boolean doRecipeSearch;

	public CenserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemHandler = new InvWrapper(items);
		doRecipeSearch = true;
	}

	@Override
	public void tick() {

	}

	public boolean tryAddItem(ItemStack stack) {
		return false;//TODO
	}


	public List<Container> getContainers() {
		return List.of(this.items);
	}

	public void popItems() {
		if (this.level != null) {
			Containers.dropContents(this.level, this.getBlockPos().above(), this.items);
			this.notifyTile();
		}
	}

	public void notifyTile() {
		this.setChanged();
		this.sync();
		this.doRecipeSearch = true;
	}

	@Override
	public TileTooltip getImage() {
		return new TileTooltip(List.of(), List.of());
	}

	@Override
	public List<Component> lines() {
		return List.of();
	}

	public IItemHandler getItemHandler(@Nullable Direction direction) {
		return this.itemHandler;
	}

}
