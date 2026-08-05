package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.ServerCharacterDialogManager;
import dev.xkmc.gensokyolegacy.content.rpg.handle.ClientHandle;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.l2menustacker.init.L2MenuStacker;
import dev.xkmc.l2menustacker.screen.packets.CacheMouseToClient;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FirstDialogMenu extends DialogMenu {

	public static FirstDialogMenu fromNetwork(MenuType<?> menu, int wid, Inventory inv, @Nullable RegistryFriendlyByteBuf buf) {
		YoukaiEntity ch = null;
		var player = inv.player;
		List<ClientHandle> options = new ArrayList<>();
		if (buf != null) {
			int uid = buf.readVarInt();
			int size = buf.readVarInt();
			if (player.level().getEntity(uid) instanceof YoukaiEntity e) {
				ch = e;
			}
			for (int i = 0; i < size; i++) {
				options.add(ClientHandle.STREAM_CODEC.decode(buf));
			}
		}
		return new FirstDialogMenu(menu, wid, player, ch, options);
	}

	public static FirstDialogMenu create(MenuType<?> menu, int wid, ServerPlayer sp, YoukaiEntity character) {
		var data = ServerCharacterDialogManager.get(sp.serverLevel(), character.getType());
		var handles = data.getInitialConversation(sp, character);
		var options = new ArrayList<ClientHandle>();
		for (var e : handles)
			options.add(new ClientHandle(e.display(), e.getQuest()));
		return new FirstDialogMenu(menu, wid, sp, character, handles, options);
	}

	private final List<ClientHandle> options;

	private final @Nullable List<IDialogHandle> handles;

	public FirstDialogMenu(MenuType<?> menu, int wid, ServerPlayer sp, YoukaiEntity character, List<IDialogHandle> handles, List<ClientHandle> options) {
		super(menu, wid, sp, character);
		this.handles = handles;
		this.options = options;
	}

	public FirstDialogMenu(MenuType<?> menu, int wid, Player player, @Nullable YoukaiEntity character, List<ClientHandle> options) {
		super(menu, wid, player, character);
		this.options = options;
		handles = null;
	}

	@Override
	public boolean clickMenuButton(Player player, int index) {
		if (index >= 0 && index < options.size()) {
			if (handles != null && character != null && player instanceof ServerPlayer sp && index < handles.size()) {
				L2MenuStacker.PACKET_HANDLER.toClientPlayer(new CacheMouseToClient(), sp);
				handles.get(index).openMenu(sp, character);
			}
			return true;
		}
		return super.clickMenuButton(player, index);
	}

	@Override
	public List<Component> getOptions() {
		List<Component> ans = new ArrayList<>();
		for (var e : options)
			ans.add(e.display());
		return ans;
	}

	@Override
	public Optional<Component> getBodyText() {
		return Optional.empty();
	}

}
