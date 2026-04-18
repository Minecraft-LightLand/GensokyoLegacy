package dev.xkmc.gensokyolegacy.content.item.character;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CirnoWingsItem extends TouhouWingsItem {

    public CirnoWingsItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext level, List<Component> list, TooltipFlag flag) {
        //list.add(GLLang.ITEM$USAGE_FAIRY_WINGS.get(GLMechanics.ICE_FAIRY.get().getName()));
    }

}
