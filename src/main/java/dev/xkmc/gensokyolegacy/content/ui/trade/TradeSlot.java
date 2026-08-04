package dev.xkmc.gensokyolegacy.content.ui.trade;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;

public class TradeSlot extends Slot {

	public TradeSlot(Container p_40223_, int p_40224_, int p_40225_, int p_40226_) {
		super(p_40223_, p_40224_, p_40225_, p_40226_);
	}

	@Override
	public boolean isHighlightable() {
		return !getItem().isEmpty();
	}

	@Override
	public boolean allowModification(Player player) {
		return false;
	}

	@Override
	public boolean isFake() {
		return true;
	}

}
