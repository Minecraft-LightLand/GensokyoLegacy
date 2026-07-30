package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record SimpleDialogProvider(
		ServerPlayer sp, YoukaiEntity ch, IDialogHandle handle, Holder<Dialog> dialog
) implements MenuProvider {

	@Override
	public Component getDisplayName() {
		return Component.empty();
	}

	public void open() {
		sp.openMenu(this, this::write);
	}

	private void write(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(ch.getId());
		buf.writeResourceKey(dialog.unwrapKey().orElseThrow());
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player pl) {
		return new SimpleDialogMenu(GLMisc.DIALOG_SIMPLE.get(), wid, sp, ch, handle, dialog);
	}

}
