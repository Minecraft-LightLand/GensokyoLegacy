package dev.xkmc.gensokyolegacy.compat.food.flesh;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FleshSimpleItem extends Item {

	public FleshSimpleItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public Component getName(ItemStack pStack) {
		return Component.translatable(this.getDescriptionId(pStack),
				RolePlayHandler.showInfo() ?
						YHLangData.FLESH_NAME_YOUKAI.get() :
						YHLangData.FLESH_NAME_HUMAN.get()
		);
	}

}