package dev.xkmc.gensokyolegacy.content.item.debug;

import dev.xkmc.gensokyolegacy.init.data.GLLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugGlasses extends Item {

	public DebugGlasses(Properties properties) {
		super(properties);
	}

	@Override
	public @Nullable EquipmentSlot getEquipmentSlot(ItemStack stack) {
		return EquipmentSlot.HEAD;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> list, TooltipFlag flag) {
		list.add(GLLang.ITEM_GLASS_PATH.get().withStyle(ChatFormatting.GRAY));

		list.add(GLLang.ITEM_GLASS_CHARACTER.get().withStyle(ChatFormatting.GRAY));
		list.add(GLLang.ITEM_GLASS_BED.get().withStyle(ChatFormatting.GRAY));
	}

}
