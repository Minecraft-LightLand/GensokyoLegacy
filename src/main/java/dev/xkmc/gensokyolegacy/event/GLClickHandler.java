package dev.xkmc.gensokyolegacy.event;

import dev.xkmc.gensokyolegacy.content.item.tool.InvClickItem;
import dev.xkmc.l2menustacker.click.writable.ClickedPlayerSlotResult;
import dev.xkmc.l2menustacker.click.writable.WritableStackClickHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GLClickHandler extends WritableStackClickHandler {

	public GLClickHandler(ResourceLocation rl) {
		super(rl);
	}

	@Override
	protected void handle(ServerPlayer sp, ClickedPlayerSlotResult res) {
		if (res.stack().getItem() instanceof InvClickItem item) {
			item.handleClick(sp, res.stack());
			res.container().update();
		}
	}

	@Override
	public boolean isAllowed(ItemStack stack) {
		return stack.getItem() instanceof InvClickItem;
	}
}
