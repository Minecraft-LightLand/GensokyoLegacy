package dev.xkmc.gensokyolegacy.content.block.shelf;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class ShelfBlockEntity extends BaseBlockEntity {

	@SerialField
	public ItemStack stack = ItemStack.EMPTY;
	@SerialField
	public int cost;

	public ShelfBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

}
