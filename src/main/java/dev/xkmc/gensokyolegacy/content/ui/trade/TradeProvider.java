package dev.xkmc.gensokyolegacy.content.ui.trade;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record TradeProvider(
		ServerPlayer sp, YoukaiEntity ch
) implements MenuProvider {

	public static void open(ServerPlayer sp, YoukaiEntity ch) {
		new TradeProvider(sp, ch).open();
	}

	@Override
	public Component getDisplayName() {
		return Component.empty();
	}

	public void open() {
		sp.openMenu(this, this::write);
	}

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(ch.getId());
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player pl) {
		return new TradeMenu(GLMisc.TRADE.get(), wid, sp, ch);
	}

}
