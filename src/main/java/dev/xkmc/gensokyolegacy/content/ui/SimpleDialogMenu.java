package dev.xkmc.gensokyolegacy.content.ui;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.core.CodecRegistry;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.l2core.base.menu.data.BoolArrayDataSlot;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleDialogMenu extends DialogMenu {

	public static SimpleDialogMenu fromNetwork(MenuType<?> menu, int wid, Inventory inv, @Nullable RegistryFriendlyByteBuf buf) {
		YoukaiEntity ch = null;
		var player = inv.player;
		Holder<Dialog> dialog = null;
		if (buf != null) {
			int uid = buf.readVarInt();
			var id = buf.readResourceLocation();
			if (player.level().getEntity(uid) instanceof YoukaiEntity e) {
				ch = e;
			}
			var opt = player.level().registryAccess().holder(ResourceKey.create(CodecRegistry.DIALOG.key(), id));
			if (opt.isPresent())
				dialog = opt.get();
		}
		return new SimpleDialogMenu(menu, wid, player, ch, null, dialog);
	}

	public final @Nullable IDialogHandle handle;
	private final BoolArrayDataSlot conditions;

	protected @Nullable Holder<Dialog> dialog;
	protected @Nullable List<DialogOption<?>> options;

	protected SimpleDialogMenu(MenuType<?> menu, int wid, Player player, @Nullable YoukaiEntity ch, @Nullable IDialogHandle handle, @Nullable Holder<Dialog> dialog) {
		super(menu, wid, player, ch);
		this.handle = handle;
		conditions = new BoolArrayDataSlot(this, 16);
		if (dialog != null)
			setDialog(dialog);
	}

	public void setDialog(Holder<Dialog> dialog) {
		this.dialog = dialog;
		options = dialog.value().options();
		for (int i = 0; i < 16; i++)
			conditions.set(false, i);
		if (player instanceof ServerPlayer sp && character != null) {
			for (int i = 0; i < options.size(); i++) {
				conditions.set(options.get(i).match(sp, character), i);
			}
		}
	}

	@Override
	public boolean clickMenuButton(Player pl, int index) {
		if (options != null && index >= 0 && index <= options.size()) {
			if (conditions.get(index)) {
				var next = options.get(index).next();
				if (next.isPresent()) {
					setDialog(next.get());
				} else if (!(pl instanceof ServerPlayer))
					pl.closeContainer();
				return true;
			}
		}
		return super.clickMenuButton(pl, index);
	}

	public List<Component> getOptions() {
		List<Component> ans = new ArrayList<>();
		if (options == null) return ans;
		for (int i = 0; i < options.size(); i++) {
			if (conditions.get(i)) {
				ans.add(options.get(i).display());
			}
		}
		return ans;
	}

}
