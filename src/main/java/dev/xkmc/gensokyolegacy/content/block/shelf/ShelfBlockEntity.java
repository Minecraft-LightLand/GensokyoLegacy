package dev.xkmc.gensokyolegacy.content.block.shelf;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.UUID;

@SerialClass
public class ShelfBlockEntity extends BaseBlockEntity {

	@SerialField
	public UUID owner = Util.NIL_UUID;
	@SerialField
	public ItemStack stack = ItemStack.EMPTY;
	@SerialField
	public int cost, stock, earning;


	public ShelfBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean set(Level level, ItemStack held, boolean isCreative) {
		if (!stack.isEmpty()) {
			if (held.isEmpty()) {
				if (!level.isClientSide()) {
					Block.popResource(level, getBlockPos(), stack.copyWithCount(stock));
					stack = ItemStack.EMPTY;
					notifyTile();
				}
				return true;
			}
			if (held.is(Items.EMERALD)) {
				if (!level.isClientSide()) {
					cost = held.getCount();
					notifyTile();
				}
				return true;
			}
			if (held.is(Items.EMERALD_BLOCK)) {
				if (!level.isClientSide()) {
					cost = held.getCount() * 9;
					notifyTile();
				}
				return true;
			}
		} else if (held.isEmpty()) return false;
		if (!level.isClientSide()) {
			if (!ItemStack.isSameItemSameComponents(stack, held)) {
				Block.popResource(level, getBlockPos(), stack.copyWithCount(stock));
				stack = held.copyWithCount(1);
				stock = held.getCount();
			} else stock += held.getCount();
			held.shrink(stock);
			cost = 1;
			notifyTile();
		}
		return true;
	}

	public void notifyTile() {
		sync();
		setChanged();
	}

	public Component getTitle() {
		return Component.literal("¥" + cost + " (" + stock + ")");
	}

	public boolean buy(Level level, ItemStack held, Player player) {
		if (stack.isEmpty() || stock <= 0) return false;
		int emerald = 0, blocks = 0;
		for (var e : player.getInventory().items) {
			if (e.is(Items.EMERALD)) emerald += e.getCount();
			if (e.is(Items.EMERALD_BLOCK)) blocks += e.getCount();
		}
		if (emerald + blocks * 9 < cost) return false;
		if (!level.isClientSide()) {
			stock--;
			earning += cost;

			int emeraldCost = Math.min(emerald, cost);
			int blockCost = Math.min(blocks, ((cost - emeraldCost) + 8) / 9);
			emeraldCost = cost - blockCost * 9;
			for (var e : player.getInventory().items) {
				if (e.is(Items.EMERALD_BLOCK) && blockCost > 0) {
					int remove = Math.min(blockCost , e.getCount());
					e.shrink(remove);
					blockCost -= remove * 9;
				}
			}
			if (emeraldCost < 0) {
				player.getInventory().placeItemBackInInventory(new ItemStack(Items.EMERALD, -emeraldCost));
			} else if (emeraldCost > 0) {
				for (var e : player.getInventory().items) {
					if (e.is(Items.EMERALD) && emeraldCost > 0) {
						int remove = Math.min(emeraldCost, e.getCount());
						e.shrink(remove);
						emeraldCost -= remove;
					}
				}
			}
			player.getInventory().placeItemBackInInventory(stack.copyWithCount(1));
			notifyTile();
		}
		return true;
	}

}
