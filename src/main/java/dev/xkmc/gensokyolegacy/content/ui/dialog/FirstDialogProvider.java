package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.ServerCharacterDialogManager;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.gensokyolegacy.init.registrate.GLMisc;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.ArrayList;
import java.util.List;

public record FirstDialogProvider(
		ServerPlayer sp, YoukaiEntity ch, List<IDialogHandle> handles, List<Component> options
) implements MenuProvider {

	public static void open(ServerPlayer sp, YoukaiEntity ch) {
		var handles = ServerCharacterDialogManager.get(sp.serverLevel(), ch.getType()).getInitialConversation(sp, ch);
		List<Component> options = new ArrayList<>();
		for (var e : handles) {
			options.add(e.display());
		}
		new FirstDialogProvider(sp, ch, handles, options).open();
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
		buf.writeVarInt(options.size());
		for (var e : options) {
			ComponentSerialization.STREAM_CODEC.encode(buf, e);
		}
	}

	@Override
	public AbstractContainerMenu createMenu(int wid, Inventory inv, Player pl) {
		return new FirstDialogMenu(GLMisc.DIALOG_FIRST.get(), wid, sp, ch, handles, options);
	}

}
