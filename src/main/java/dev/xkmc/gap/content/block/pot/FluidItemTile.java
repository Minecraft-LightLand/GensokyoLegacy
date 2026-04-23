package dev.xkmc.gap.content.block.pot;

import dev.xkmc.l2core.base.tile.BaseTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

public interface FluidItemTile {

	static ItemInteractionResult addFluidOrItem(FluidItemTile be, ItemStack stack, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!be.mayModify()) return ItemInteractionResult.FAIL;
		// fill water from bottle
		if (stack.is(Items.POTION)) {
			var potion = stack.get(DataComponents.POTION_CONTENTS);
			if (potion != null && potion.is(Potions.WATER)) {
				var attempt = be.getFluidHandler().fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.SIMULATE);
				if (attempt == 250) {
					if (level instanceof ServerLevel sl) {
						be.getFluidHandler().fill(new FluidStack(Fluids.WATER, 250), IFluidHandler.FluidAction.EXECUTE);
						if (!player.isCreative()) {
							stack.shrink(1);
							player.getInventory().placeItemBackInInventory(Items.GLASS_BOTTLE.getDefaultInstance());
						}
						sl.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 0.7f, 1);
					}
					return ItemInteractionResult.SUCCESS;
				}
			}
		}
		return addItem(be, stack, level, pos);
	}

	static ItemInteractionResult addItem(FluidItemTile be, ItemStack stack, Level level, BlockPos pos) {
		if (!be.mayModify()) return ItemInteractionResult.FAIL;
		ItemStack copy = stack.copyWithCount(1);
		if (be.getItemHandler().canAddItem(copy)) {
			if (level instanceof ServerLevel sl) {
				ItemStack remain = be.addItem(copy);
				if (remain.isEmpty()) {
					stack.shrink(1);
					be.notifyTile();
					sl.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 0.7f, 1);
				}
			}
			return ItemInteractionResult.SUCCESS;
		}
		return ItemInteractionResult.CONSUME;
	}

	default boolean mayModify() {
		return true;
	}

	default ItemStack addItem(ItemStack copy) {
		return getItemHandler().addItem(copy);
	}

	BaseTank getFluidHandler();

	SimpleContainer getItemHandler();

	void notifyTile();

	default @Nullable IFluidHandler getFluidCap(@Nullable Direction dir) {
		return getFluidHandler();
	}


}
