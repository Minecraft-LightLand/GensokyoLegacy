package dev.xkmc.gensokyolegacy.content.ui.trade;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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

	public final Player player;
	public final @Nullable YoukaiEntity character;

	public static TradeMenu fromNetwork(MenuType<?> menu, int wid, Inventory inv, @Nullable RegistryFriendlyByteBuf buf) {
		YoukaiEntity ch = null;
		var player = inv.player;
		if (buf != null) {
			int uid = buf.readVarInt();
			if (player.level().getEntity(uid) instanceof YoukaiEntity e) {
				ch = e;
			}
		}
		return new TradeMenu(menu, wid, player, ch);
	}

	public static TradeMenu create(MenuType<?> menu, int wid, ServerPlayer sp, YoukaiEntity character) {
		return new TradeMenu(menu, wid, sp, character);
	}

	public TradeMenu(@Nullable MenuType<?> type, int wid, Player player, @Nullable YoukaiEntity character) {
		super(type, wid);
		this.player = player;
		this.character = character;
		bindPlayerInventory(player.getInventory(), 8, 84);
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
		return character != null && character.isAlive();
	}

}
