package dev.xkmc.gensokyolegacy.content.client.tab;

import dev.xkmc.l2tabs.tabs.core.TabBase;
import dev.xkmc.l2tabs.tabs.core.TabManager;
import dev.xkmc.l2tabs.tabs.core.TabToken;
import dev.xkmc.l2tabs.tabs.inventory.InvTabData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class TabRole extends TabBase<InvTabData, TabRole> {

	public TabRole(int index, TabToken<InvTabData, TabRole> token,
				   TabManager<InvTabData> manager, Component title) {
		super(index, token, manager, title);
	}

	@Override
	public void onTabClicked() {
		Minecraft.getInstance().setScreen(new RoleTabScreen(this.getMessage()));
	}

}
