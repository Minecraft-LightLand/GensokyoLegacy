package dev.xkmc.gensokyolegacy.content.attachment.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;

import java.util.function.Function;

public class HomeChestUtil {

	public static boolean isValid(@Nullable BlockEntity be) {
		return be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity || be instanceof CabinetBlockEntity;
	}

	public static void put(ServerLevel level, BlockPos chest, Function<Boolean, ItemStack> doCraft) {
		var be = level.getBlockEntity(chest);
		if (!isValid(be)) return;
		var cap = level.getCapability(Capabilities.ItemHandler.BLOCK, chest, Direction.UP);
		if (cap == null) {
			if (be instanceof BaseContainerBlockEntity cont) {
				cap = new InvWrapper(cont);
			} else return;
		}
		if (ItemHandlerHelper.insertItem(cap, doCraft.apply(true), true).isEmpty()) {
			ItemHandlerHelper.insertItem(cap, doCraft.apply(false), false);
		}
	}

}
