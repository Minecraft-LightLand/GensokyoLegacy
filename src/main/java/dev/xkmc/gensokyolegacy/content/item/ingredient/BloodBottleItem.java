package dev.xkmc.gensokyolegacy.content.item.ingredient;

import dev.xkmc.gensokyolegacy.content.attachment.role.RolePlayHandler;
import dev.xkmc.gensokyolegacy.init.data.GLLang;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeBottleItem;
import dev.xkmc.youkaishomecoming.content.item.fluid.SakeFluid;
import dev.xkmc.youkaishomecoming.init.data.YHLangData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class BloodBottleItem extends SakeBottleItem {

	public BloodBottleItem(Supplier<SakeFluid> fluid, Properties pProperties) {
		super(fluid, pProperties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
		RolePlayHandler.addTooltips(list, GLLang.OBTAIN_BLOOD.get(),//TODO
				null);
	}

}
