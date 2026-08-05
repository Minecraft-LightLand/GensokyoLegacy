package dev.xkmc.gensokyolegacy.content.ui.trade;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.ServerCharacterDialogManager;
import dev.xkmc.gensokyolegacy.content.rpg.trade.TradeOffer;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

	private final TradeSlot[] slots = new TradeSlot[15];

	private final DataSlot page;

	public TradeMenu(@Nullable MenuType<?> type, int wid, Player player, @Nullable YoukaiEntity character) {
		super(type, wid);
		this.player = player;
		this.character = character;
		page = addDataSlot(DataSlot.standalone());
		bindPlayerInventory(player.getInventory(), 8, 84);
		for (int i = 0; i < 15; i++) {
			addSlot(slots[i] = new TradeSlot(cont, i, -1 + i % 5 * 36, 10 + i / 5 * 36));
		}
		if (player instanceof ServerPlayer)
			refreshOffers();
	}

	private void refreshOffers() {
		var list = getOffers();
		for (int i = 0; i < 15; i++) {
			int index = page.get() * 15 + i;
			if (index >= list.size()) {
				slots[i].set(ItemStack.EMPTY);
			} else {
				var offer = list.get(index);
				slots[i].set(TradeOffer.toIcon(offer));
			}
		}
	}

	private List<Holder<TradeOffer>> getOffers() {
		if (character == null || !(player instanceof ServerPlayer sp)) return List.of();
		return ServerCharacterDialogManager.get(sp.serverLevel(), character.getType()).getTradeOffers(sp, character);
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
