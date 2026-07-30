package dev.xkmc.gensokyolegacy.content.ui;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.Dialog;
import dev.xkmc.gensokyolegacy.content.rpg.dialog.DialogOption;
import dev.xkmc.gensokyolegacy.content.rpg.handle.IDialogHandle;
import dev.xkmc.l2core.base.menu.data.BoolArrayDataSlot;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SimpleDialogMenu extends DialogMenu {

	public final @Nullable IDialogHandle handle;
	private final BoolArrayDataSlot conditions;

	protected Dialog dialog;
	protected List<DialogOption<?>> options;

	protected SimpleDialogMenu(MenuType<?> menu, int wid, Player player, @Nullable YoukaiEntity ch, Dialog dialog, @Nullable IDialogHandle handle) {
		super(menu, wid, player, ch);
		this.handle = handle;
		conditions = new BoolArrayDataSlot(this, 16);
		setDialog(dialog);
	}

	public void setDialog(Dialog dialog) {
		this.dialog = dialog;
		options = dialog.options();
		if (player instanceof ServerPlayer sp && character != null) {
			for (int i = 0; i < 16; i++)
				conditions.set(false, i);
			for (int i = 0; i < options.size(); i++) {
				conditions.set(options.get(i).match(sp, character), i);
			}
		}
	}

	public List<Component> getOptions() {
		List<Component> ans = new ArrayList<>();
		for (int i = 0; i < options.size(); i++) {
			if (conditions.get(i)) {
				ans.add(options.get(i).display());
			}
		}
		return ans;
	}

}
