package dev.xkmc.gensokyolegacy.content.ui.dialog;

import dev.xkmc.gensokyolegacy.content.entity.youkai.YoukaiEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public abstract class DialogMenu extends AbstractContainerMenu {

	public final Player player;
	public final @Nullable YoukaiEntity character;

	protected DialogMenu(MenuType<?> menu, int wid, Player player, @Nullable YoukaiEntity character) {
		super(menu, wid);
		this.player = player;
		this.character = character;
	}

	public abstract List<Component> getOptions();

	public abstract Optional<Component> getBodyText();

	@Override
	public ItemStack quickMoveStack(Player player, int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		if (!player.isAlive()) return false;
		if (character == null || !character.isAlive()) return false;
		if (!(player instanceof ServerPlayer sp)) return true;
		return character.isTalkingTo(sp);
	}

}
