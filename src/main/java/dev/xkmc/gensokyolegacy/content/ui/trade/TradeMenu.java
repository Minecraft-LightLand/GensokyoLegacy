package dev.xkmc.gensokyolegacy.content.ui.trade;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TradeMenu extends AbstractContainerMenu {

	private final SimpleContainer cont = new SimpleContainer(15);

	protected TradeMenu(@Nullable MenuType<?> type, int wid, Inventory inv) {
		super(type, wid);
		bindPlayerInventory(inv, 8, 84);
		for (int i = 0; i < 15; i++) {
			addSlot(new TradeSlot(cont, i, -1 + i % 5 * 36, 10 + i / 5 * 36));
		}
	}

	public void bindPlayerInventory(Inventory inv, int invX, int invY) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				this.addSlot(new Slot(inv, j + i * 9 + 9, invX + j * 18, invY + i * 18));
			}
		}
		for (int k = 0; k < 9; k++) {
			this.addSlot(new Slot(inv, k, invX + k * 18, invY + 58));
		}
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}

}
