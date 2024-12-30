package dev.xkmc.gensokyolegacy.compat.food.flesh;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class FleshBlockItem extends BlockItem {

	public FleshBlockItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
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
